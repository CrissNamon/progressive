package ru.danilarassokhin.progressive.basic;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.annotation.Components;
import ru.danilarassokhin.progressive.annotation.Configuration;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.injection.BasicGameBeanFactory;
import ru.danilarassokhin.progressive.basic.injection.Bean;
import ru.danilarassokhin.progressive.basic.injection.SimplePackageLoader;
import ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner;
import ru.danilarassokhin.progressive.basic.util.BasicComponentCreator;
import ru.danilarassokhin.progressive.exception.*;
import ru.danilarassokhin.progressive.injection.*;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic DI container implementation.
 */
public final class BasicDIContainer implements DIContainer {

  private final Map<Class<?>, Map<String, Bean>> beans;
  private final Set<Method> viewedMethods;

  private final GameBeanFactory gameBeanFactory;

  protected BasicDIContainer() {
    BasicComponentManager.getGameLogger().info("Progressive DI initialization...\n");
    beans = new ConcurrentHashMap<>();
    viewedMethods = Collections.synchronizedSet(new HashSet<>());
    gameBeanFactory = new BasicGameBeanFactory();
  }

  @Override
  public synchronized void init() {
    PackageLoader packageLoader = new SimplePackageLoader();
    PackageScanner packageScanner = new SimplePackageScanner();
    init(packageLoader, packageScanner);
  }

  @Override
  public synchronized void init(PackageLoader packageLoader, PackageScanner packageScanner) {
    Package[] packages = packageLoader.loadAllPackages();
    for (Package p : packages) {
      scanPackage(p.getName(), packageScanner);
    }
    for (Class<?> beanClass : beans.keySet()) {
      if (!beanClass.isInterface()) {
        createBeanFromClass(beanClass);
      }
    }
  }

  @Override
  public synchronized void scanPackage(String name, PackageScanner loader) {
    Set<Class<?>> classesInPackage = loader.findAllClassesIn(name);
    Set<Class<?>> beans = classesInPackage.stream().parallel()
        .unordered()
        .filter(c -> c.isAnnotationPresent(Configuration.class))
        .collect(Collectors.toSet());
    for (Class<?> bean : beans) {
      loadConfiguration(bean, loader);
    }
    beans = classesInPackage.stream().parallel().unordered()
        .filter(c -> ComponentAnnotationProcessor.isAnnotationPresent(GameBean.class, c))
        .collect(Collectors.toSet());
    for (Class<?> bean : beans) {
      createBeanMetaInformation(bean);
    }
  }

  @Override
  public synchronized void loadBean(Class<?> beanClass) {
    createBeanMetaInformation(beanClass);
    loadBeanFrom(beanClass);
  }

  @Override
  public <V> V getBean(Class<V> beanClass) {
    Map<String, Bean> beansOfClass  = beans.get(beanClass);
    if (beansOfClass == null) {
      throw new BeanNotFoundException("There is no beans for "
          + beanClass.getName() + " were found!", beanClass);
    }
    if (beansOfClass.entrySet().size() > 1) {
      throw new BeanConflictException("There are more than one beans of " + beanClass.getName()
      + " found! What to inject?");
    }
    Map.Entry<String, Bean> beanEntry = beansOfClass.entrySet().stream()
        .parallel()
        .unordered()
        .findFirst().orElse(null);
    if (beanEntry == null) {
      throw new BeanNotFoundException("There is no beans for "
          + beanClass.getName() + " were found!", beanClass);
    }
    return getBean(beanEntry.getKey(), beanClass);
  }

  @Override
  public <V> V getBean(String name, Class<V> beanClass) {
    if (!beans.containsKey(beanClass)) {
      throw new BeanNotFoundException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!", beanClass, name);
    }
    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new ConcurrentHashMap<>());
    Bean bean = beansOfClass.getOrDefault(name, null);
    if (bean == null) {
      throw new BeanNotFoundException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!", beanClass, name);
    }
    if (!bean.isReady()) {
      throw new BeanCircularDependencyException("GameBean " + name + " of class "
          + beanClass.getName() + " is not ready! Is there a circular dependency?", beanClass);
    }
    if (!bean.haveObject() && !bean.isCreated() && bean.isClass()) {
      BasicComponentManager
          .getGameLogger()
          .info("GameBean " + name + " of type " + beanClass.getName() + " has not been created yet. Creating..");
      createBeanFromClass(bean.getRealType());
    }
    V exists = (V) bean.getBean();
    if (exists == null && bean.getCreationPolicy().equals(GameBeanCreationPolicy.SINGLETON)) {
      throw new BeanNotFoundException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!", beanClass, name);
    }
    if (bean.getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
      exists = (V) updateObjectTypeBean(bean);
      bean.setBean(exists);
      updateRealTypeInterfaces(name, bean.getRealType(), beansOfClass, bean);
    }
    return exists;
  }

  @Override
  public synchronized <V> Optional<V> searchBean(Class<V> beanClass) {
    try {
      return Optional.of(
          getBean(beanClass)
      );
    } catch (RuntimeException e) {
      return Optional.empty();
    }
  }

  @Override
  public synchronized <V> Optional<V> searchBean(String name, Class<V> beanClass) {
    try {
      return Optional.of(
          getBean(name, beanClass)
      );
    } catch (RuntimeException e) {
      return Optional.empty();
    }
  }

  @Override
  public synchronized void loadConfiguration(Class<?> configClass) throws BeanUndefinedException {
    loadConfiguration(configClass, new SimplePackageScanner());
  }

  @Override
  public void loadConfiguration(Class<?> config, PackageScanner scanner) throws BeanUndefinedException {
    Method[] methods = config.getDeclaredMethods();
    Arrays.asList(methods).removeIf(m -> !m.isAnnotationPresent(GameBean.class));
    Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
    Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(GameBean.class).order()));
    Object configObj = BasicComponentCreator.create(config);
    for (Method method : methods) {
      if (viewedMethods.contains(method)) {
        continue;
      }
      if (method.isAnnotationPresent(GameBean.class)) {
        BasicComponentManager
            .getGameLogger().info("Found GameBean annotation in " + config.getName()
            + " in method " + method.getName()
            + ". Trying to make bean...");
        try {
          createBeanFromMethod(method, configObj);
        } catch (Throwable t) {
          throw new BeanUndefinedException(t.getMessage(), t);
        }
      }
    }
    if (config.isAnnotationPresent(Components.class)) {
      Components components = config.getAnnotation(Components.class);
      Class<?>[] componentsClasses = components.value();
      for (Class<?> componentClass : componentsClasses) {
        createBeanMetaInformation(componentClass);
      }
    }
    if (config.isAnnotationPresent(ComponentScan.class)) {
      ComponentScan componentScan = config.getAnnotation(ComponentScan.class);
      String[] packages = componentScan.value();
      scanPackages(packages, scanner);
    }
  }

  private synchronized void createBeanMetaInformation(Class<?> beanClass) {
    GameBean gameBean = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    if (gameBean == null) {
      throw new AnnotationException("Tried to create bean from "
          + beanClass
          + ", but it has no @GameBean annotation. Bean needs it, actually...");
    }
    Bean information = gameBeanFactory.createBeanMetaInformationFromClass(beanClass);
    String beanName = gameBean.name();
    if (beanName.isEmpty()) {
      beanName = beanClass.getSimpleName().toLowerCase();
    }
    saveBeanMetaInformation(beanName, information);
  }

  private void saveBeanMetaInformation(String name, Bean metaInformation) {
    Map<String, Bean> componentBeans = beans.getOrDefault(metaInformation.getRealType(), new ConcurrentHashMap<>());
    if (componentBeans.containsKey(name)) {
      throw new BeanDuplicationException("GameBean name duplication ("
          + name + ") for "
          + metaInformation.getRealType().getName());
    }
    componentBeans.putIfAbsent(name, metaInformation);
    beans.putIfAbsent(metaInformation.getRealType(), componentBeans);
    Class<?>[] interfaces = metaInformation.getRealType().getInterfaces();
    for (Class<?> interfaceClass : interfaces) {
      componentBeans = beans.getOrDefault(interfaceClass, new ConcurrentHashMap<>());
      if (componentBeans.containsKey(name)) {
        throw new BeanDuplicationException("GameBean name duplication ("
            + name + ") for " + interfaceClass.getName());
      }
      componentBeans.putIfAbsent(name, metaInformation);
      beans.putIfAbsent(interfaceClass, componentBeans);
    }
  }

  private synchronized void loadBeanFrom(Class<?> beanClass) {
    viewedMethods.clear();
    GameBean annotation = ComponentAnnotationProcessor
        .findAnnotation(beanClass, GameBean.class);
    if (annotation != null) {
      createBeanFromClass(beanClass);
    } else {
      throw new AnnotationException("Tried to create bean from "
          + beanClass
          + ", but it has no @GameBean annotation. Bean needs it, actually...");
    }
  }

  private void markBeanAsCreated(String name, Class<?> beanClass) {
    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
    Bean beanData = beansOfClass.get(name);
    if (beanData == null) {
      return;
    }
    beanData.setCreated(true);
    updateRealTypeInterfaces(name, beanClass, beansOfClass, beanData);
  }

  private void setBeanReadyStatus(String name, Class<?> beanClass, boolean status) {
    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new ConcurrentHashMap<>());
    Bean beanData = beansOfClass.get(name);
    if (beanData == null) {
      return;
    }
    beanData.setReady(status);
    updateRealTypeInterfaces(name, beanClass, beansOfClass, beanData);
  }

  private void updateRealTypeInterfaces(String name, Class<?> beanClass, Map<String, Bean> beansOfClass, Bean beanData) {
    Class<?>[] interfaces = beanClass.getInterfaces();
    beansOfClass.put(name, beanData);
    beans.putIfAbsent(beanClass, beansOfClass);
    for (Class<?> inter : interfaces) {
      beansOfClass = beans.getOrDefault(inter, new ConcurrentHashMap<>());
      beansOfClass.putIfAbsent(name, beanData);
      beans.putIfAbsent(inter, beansOfClass);
    }
  }

  private void updateBeanObject(String name, Class<?> beanClass, Object bean) {
    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
    Bean beanData = beansOfClass.get(name);
    if (beanData == null) {
      return;
    }
    beanData.setBean(bean);
    updateRealTypeInterfaces(name, beanClass, beansOfClass, beanData);
  }

  private void createBeanFromClass(Class<?> beanClass) {
    GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    if (annotation == null) {
      return;
    }
    BasicComponentManager
        .getGameLogger().info("Found @GameBean annotation in "
        + beanClass.getName() + ". Trying to make bean...");
    String name = annotation.name();
    if (name.isEmpty()) {
      name = beanClass.getSimpleName().toLowerCase();
    }
    setBeanReadyStatus(name, beanClass, false);
    Object bean = BasicComponentCreator.create(beanClass);
    updateBeanObject(name, beanClass, bean);
    setBeanReadyStatus(name, beanClass, true);
    markBeanAsCreated(name, beanClass);
    BasicComponentManager
        .getGameLogger().info("GameBean with name " + name + " created for " + beanClass.getName());
    BasicComponentManager
        .getGameLogger().log("", "");
  }

  private void createBeanFromMethod(Method m, Object o) {
    if (viewedMethods.contains(m)) {
      return;
    }
    m.setAccessible(true);
    if (!m.getReturnType().equals(Void.TYPE)) {
      GameBean annotation = m.getAnnotation(GameBean.class);
      String name = annotation.name();
      if (name.isEmpty()) {
        name = m.getName().toLowerCase();
      }
      if (checkIfBeanExists(name, m.getReturnType())) {
        throw new BeanDuplicationException("GameBean name duplication "
            + name + " in " + o.getClass().getName());
      }
      Bean beanData = invoke(m, o);
      viewedMethods.add(m);
      beanData.setCreationPolicy(annotation.policy());
      beanData.setMethod(m);
      beanData.setMethodCaller(o);
      beanData.setRealType(beanData.getBean().getClass());
      Map<String, Bean> beansOfClass = beans.getOrDefault(
          m.getReturnType(), new HashMap<>()
      );
      updateRealTypeInterfaces(name, beanData.getRealType(), beansOfClass, beanData);
      BasicComponentManager
          .getGameLogger().info("GameBean with name " + name + " created for "
          + beanData.getBean().getClass().getName() + " from method " + m.getName());
      BasicComponentManager
          .getGameLogger().log("", "");
    }
  }

  private boolean checkIfBeanExists(String name, Class<?> beanClass) {
    Map<String, Bean> beansOfClass = beans.get(beanClass);
    if (beansOfClass == null) {
      return false;
    }
    Bean bean = beansOfClass.get(name);
    if (bean == null) {
      return false;
    }
    Class<?>[] interfaces = beanClass.getInterfaces();
    for (Class<?> inter : interfaces) {
      checkIfBeanExists(name, inter);
    }
    return true;
  }

  private Bean invoke(Method m, Object obj) throws ArrayIndexOutOfBoundsException {
    GameBean annotation = m.getAnnotation(GameBean.class);
    Object[] args = BasicComponentCreator.injectBeansToParameters(m.getReturnType(), m.getParameterTypes(), m.getParameterAnnotations());
    Object methodResult = BasicComponentCreator.invoke(m, obj, args);
    Bean beanData = new Bean();
    beanData.setBean(methodResult);
    beanData.setCreationPolicy(annotation.policy());
    beanData.setMethod(m);
    beanData.setMethodArgs(args);
    viewedMethods.add(m);
    return beanData;
  }


  private Object updateObjectTypeBean(Bean bean) {
    if (!bean.isClass()) {
      bean.setMethodArgs(
          BasicComponentCreator.injectBeansToParameters(
              bean.getRealType(),
              bean.getMethod().getParameterTypes(),
              bean.getMethod().getParameterAnnotations()
          )
      );
      return BasicComponentCreator.invoke(bean.getMethod(), bean.getMethodCaller(), bean.getMethodArgs());
    } else {
      return BasicComponentCreator.create(bean.getRealType());
    }
  }

  private void scanPackages(String[] packages, PackageScanner loader) {
    for (String p : packages) {
      scanPackage(p, loader);
    }
  }

}
