package ru.hiddenproject.progressive.basic;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import ru.hiddenproject.progressive.annotation.ComponentScan;
import ru.hiddenproject.progressive.annotation.Components;
import ru.hiddenproject.progressive.annotation.Configuration;
import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.injection.*;
import ru.hiddenproject.progressive.basic.util.BasicComponentCreator;
import ru.hiddenproject.progressive.exception.*;
import ru.hiddenproject.progressive.injection.*;
import ru.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic DI container implementation.
 */
public final class BasicDIContainer implements DIContainer {

  private final String variant;
  private final Map<BeanKey, Bean> beans;
  private final Set<Method> viewedMethods;

  private final GameBeanFactory gameBeanFactory;

  public BasicDIContainer() {
    BasicComponentManager.getGameLogger().info("Progressive DI initialization...\n");
    beans = new ConcurrentHashMap<>();
    viewedMethods = Collections.synchronizedSet(new HashSet<>());
    gameBeanFactory = new BasicGameBeanFactory();
    variant = GameBean.DEFAULT_VARIANT;
  }

  /**
   * Basic constructor.
   *
   * @param variant Variant to be used in bean creation.
   *                See {@link ru.hiddenproject.progressive.annotation.GameBean} for more information.
   */
  public BasicDIContainer(String variant) {
    BasicComponentManager.getGameLogger().info("Progressive DI initialization of "
        + variant
        + " variant"
        + "...\n");
    beans = new ConcurrentHashMap<>();
    viewedMethods = Collections.synchronizedSet(new HashSet<>());
    gameBeanFactory = new BasicGameBeanFactory();
    this.variant = variant;
  }

  @Override
  public String getVariant() {
    return variant;
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
    for (BeanKey key : beans.keySet()) {
      if (!key.getType().isInterface()) {
        createBeanFromClass(key.getType());
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
    Set<BeanKey> beansOfClass = beans.keySet().stream().parallel().unordered()
        .filter(k -> k.getType() == beanClass)
        .collect(Collectors.toSet());
    if (beansOfClass.size() == 0) {
      throw new BeanNotFoundException("There is no beans for "
          + beanClass.getName() + " were found!", beanClass);
    }
    if (beansOfClass.size() > 1) {
      beansOfClass.forEach(k -> System.out.println(k.toString()));
      throw new BeanConflictException("There are more than one beans of " + beanClass.getName()
      + " found! What to inject?");
    }
    BeanKey beanKey = beansOfClass.stream()
        .parallel()
        .unordered()
        .findFirst().orElse(new BeanKey("", null));
    Bean beanEntry = beans.get(beanKey);
    if (beanEntry == null) {
      throw new BeanNotFoundException("There is no beans for "
          + beanClass.getName() + " were found!", beanClass);
    }
    return getBean(beanKey.getName(), beanClass);
  }

  @Override
  public <V> V getBean(String name, Class<V> beanClass) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    Bean bean = beans.getOrDefault(beanKey, null);
    if (bean == null) {
      throw new BeanNotFoundException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!", beanClass, name);
    }
    if (!bean.isReady()) {
      throw new BeanCircularDependencyException("GameBean "
          + name + " of class "
          + beanClass.getName()
          + " is not ready! Is there a circular dependency?", beanClass);
    }
    if (!bean.haveObject() && !bean.isCreated() && bean.isClass()) {
      BasicComponentManager
          .getGameLogger()
          .info("GameBean "
              + name
              + " of type "
              + beanClass.getName()
              + " has not been created yet. Creating..");
      createBeanFromClass(bean.getRealType());
    }
    V exists = (V) bean.getBean();
    if (exists == null && bean.getCreationPolicy() == GameBeanCreationPolicy.SINGLETON) {
      throw new BeanNotFoundException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!", beanClass, name);
    }
    if (bean.getCreationPolicy() == GameBeanCreationPolicy.OBJECT) {
      exists = (V) updateObjectTypeBean(bean);
      bean.setBean(exists);
      updateRealTypeInterfaces(name, bean.getRealType(), bean);
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
    String beanVariant = gameBean.variant();
    if (!beanVariant.equals(variant) && !beanVariant.equals(GameBean.GLOBAL_VARIANT)) {
      return;
    }
    Bean information = gameBeanFactory.createBeanMetaInformationFromClass(beanClass);
    String beanName = gameBean.name();
    if (beanName.isEmpty()) {
      beanName = beanClass.getSimpleName().toLowerCase();
    }
    saveBeanMetaInformation(beanName, information);
  }

  private void saveBeanMetaInformation(String name, Bean metaInformation) {
    BeanKey beanKey = new BeanKey(name, metaInformation.getRealType());
    if (beans.containsKey(beanKey)) {
      throw new BeanDuplicationException("GameBean name duplication ("
          + name + ") for "
          + metaInformation.getRealType().getName());
    }
    beans.put(beanKey, metaInformation);
    Class<?>[] interfaces = metaInformation.getRealType().getInterfaces();
    for (Class<?> interfaceClass : interfaces) {
      beanKey = new BeanKey(name, interfaceClass);
      if (beans.containsKey(beanKey)) {
        throw new BeanDuplicationException("GameBean name duplication ("
            + name + ") for " + interfaceClass.getName());
      }
      beans.put(beanKey, metaInformation);
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
    BeanKey beanKey = new BeanKey(name, beanClass);
    Bean beanData = beans.get(beanKey);
    if (beanData == null) {
      return;
    }
    beanData.setCreated(true);
    updateRealTypeInterfaces(name, beanClass, beanData);
  }

  private void setBeanReadyStatus(String name, Class<?> beanClass, boolean status) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    Bean beanData = beans.get(beanKey);
    if (beanData == null) {
      return;
    }
    beanData.setReady(status);
    updateRealTypeInterfaces(name, beanClass, beanData);
  }

  private void updateRealTypeInterfaces(String name, Class<?> beanClass, Bean beanData) {
    Class<?>[] interfaces = beanClass.getInterfaces();
    BeanKey beanKey = new BeanKey(name, beanClass);
    beans.putIfAbsent(beanKey, beanData);
    for (Class<?> inter : interfaces) {
      beanKey = new BeanKey(name, inter);
      beans.putIfAbsent(beanKey, beanData);
    }
  }

  private void updateBeanObject(String name, Class<?> beanClass, Object bean) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    Bean beanData = beans.get(beanKey);
    if (beanData == null) {
      return;
    }
    beanData.setBean(bean);
    updateRealTypeInterfaces(name, beanClass, beanData);
  }

  private void createBeanFromClass(Class<?> beanClass) {
    GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    if (annotation == null) {
      return;
    }
    String beanVariant = annotation.variant();
    if (!beanVariant.equals(variant) && !beanVariant.equals(GameBean.GLOBAL_VARIANT)) {
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
        .getGameLogger().info("GameBean with name "
        + name + " created for " + beanClass.getName());
    BasicComponentManager
        .getGameLogger().log("", "");
  }

  private void createBeanFromMethod(Method m, Object o) {
    if (viewedMethods.contains(m)) {
      return;
    }
    m.setAccessible(true);
    if (m.getReturnType() != Void.TYPE) {
      GameBean annotation = m.getAnnotation(GameBean.class);
      String beanVariant = annotation.variant();
      if (!beanVariant.equals(variant) && !beanVariant.equals(GameBean.GLOBAL_VARIANT)) {
        return;
      }
      BasicComponentManager
          .getGameLogger().info("Found GameBean annotation in " + o.getClass().getName()
          + " in method " + m.getName()
          + ". Trying to make bean...");
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
      updateRealTypeInterfaces(name, beanData.getRealType(),  beanData);
      BasicComponentManager
          .getGameLogger().info("GameBean with name " + name + " created for "
          + beanData.getBean().getClass().getName() + " from method " + m.getName());
      BasicComponentManager
          .getGameLogger().log("", "");
    }
  }

  private boolean checkIfBeanExists(String name, Class<?> beanClass) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    Bean bean = beans.get(beanKey);
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
    Object[] args = BasicComponentCreator
        .injectBeansToParameters(m.getReturnType(), m.getParameterTypes(), m.getParameterAnnotations());
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
      return BasicComponentCreator
          .invoke(bean.getMethod(), bean.getMethodCaller(), bean.getMethodArgs());
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
