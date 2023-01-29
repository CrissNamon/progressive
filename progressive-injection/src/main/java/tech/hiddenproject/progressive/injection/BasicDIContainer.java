package tech.hiddenproject.progressive.injection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.ComponentScan;
import tech.hiddenproject.progressive.annotation.Components;
import tech.hiddenproject.progressive.annotation.Configuration;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.injection.BeanDefinition;
import tech.hiddenproject.progressive.basic.injection.BeanKey;
import tech.hiddenproject.progressive.exception.AnnotationException;
import tech.hiddenproject.progressive.exception.BeanCircularDependencyException;
import tech.hiddenproject.progressive.exception.BeanConflictException;
import tech.hiddenproject.progressive.exception.BeanDuplicationException;
import tech.hiddenproject.progressive.exception.BeanNotFoundException;
import tech.hiddenproject.progressive.exception.BeanUndefinedException;

/**
 * Basic DI container implementation.
 */
public final class BasicDIContainer implements DIContainer {

  private final String variant;
  private final Map<BeanKey, BeanDefinition> beans;
  private final Set<Method> viewedMethods;

  private final BeanFactory gameBeanFactory;

  private final List<BeanFactory> beanFactories = new ArrayList<>();

  private final List<BeanScanner> beanScanners = new ArrayList<>();

  public BasicDIContainer() {
    BasicComponentManager.getGameLogger().info("Progressive DI initialization...\n");
    beans = new ConcurrentHashMap<>();
    viewedMethods = Collections.synchronizedSet(new HashSet<>());
    gameBeanFactory = new GameBeanFactory();
    variant = GameBean.DEFAULT_VARIANT;
    beanScanners.add(new GameBeanScanner());
  }

  /**
   * Basic constructor.
   *
   * @param variant Variant to be used in bean creation. See {@link GameBean} for more information.
   */
  public BasicDIContainer(String variant) {
    BasicComponentManager.getGameLogger()
        .info("Progressive DI initialization of " + variant + " variant" + "...\n");
    beans = new ConcurrentHashMap<>();
    viewedMethods = Collections.synchronizedSet(new HashSet<>());
    gameBeanFactory = new GameBeanFactory();
    this.variant = variant;
    beanScanners.add(new GameBeanScanner());
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
  public <V> V getBean(String name, Class<V> beanClass) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    BeanDefinition beanDefinition = beans.getOrDefault(beanKey, null);
    if (beanDefinition == null) {
      throw new BeanNotFoundException(
          "GameBean called " + name + " for class " + beanClass.getName() + " not found!",
          beanClass,
          name
      );
    }
    if (!beanDefinition.isReady()) {
      throw new BeanCircularDependencyException(
          "GameBean "
              + name
              + " of class "
              + beanClass.getName()
              + " is not ready! Is there a circular dependency?",
          beanClass
      );
    }
    if (!beanDefinition.haveObject() && !beanDefinition.isCreated() && beanDefinition.isClass()) {
      BasicComponentManager.getGameLogger()
          .info(
              "GameBean "
                  + name
                  + " of type "
                  + beanClass.getName()
                  + " has not been created yet. Creating..");
      createBeanFromClass(beanDefinition.getRealType());
    }
    V exists = (V) beanDefinition.getBean();
    if (exists == null && beanDefinition.getCreationPolicy() == GameBeanCreationPolicy.SINGLETON) {
      throw new BeanNotFoundException(
          "GameBean called " + name + " for class " + beanClass.getName() + " not found!",
          beanClass,
          name
      );
    }
    if (beanDefinition.getCreationPolicy() == GameBeanCreationPolicy.OBJECT) {
      exists = (V) updateObjectTypeBean(beanDefinition);
      beanDefinition.setBean(exists);
      updateRealTypeInterfaces(name, beanDefinition.getRealType(), beanDefinition);
    }
    return exists;
  }

  @Override
  public <V> V getBean(Class<V> beanClass) {
    Set<BeanKey> beansOfClass =
        beans.keySet().stream()
            .parallel()
            .unordered()
            .filter(k -> k.getType() == beanClass)
            .collect(Collectors.toSet());
    if (beansOfClass.size() == 0) {
      throw new BeanNotFoundException(
          "There is no beans for " + beanClass.getName() + " were found!", beanClass);
    }
    if (beansOfClass.size() > 1) {
      throw new BeanConflictException(
          "There are more than one beans of " + beanClass.getName() + " found! What to inject?");
    }
    BeanKey beanKey =
        beansOfClass.stream().parallel().unordered().findFirst().orElse(new BeanKey("", null));
    BeanDefinition beanDefinitionEntry = beans.get(beanKey);
    if (beanDefinitionEntry == null) {
      throw new BeanNotFoundException(
          "There is no beans for " + beanClass.getName() + " were found!", beanClass);
    }
    return getBean(beanKey.getName(), beanClass);
  }

  @Override
  public synchronized <V> Optional<V> searchBean(Class<V> beanClass) {
    try {
      return Optional.of(getBean(beanClass));
    } catch (RuntimeException e) {
      return Optional.empty();
    }
  }

  @Override
  public synchronized <V> Optional<V> searchBean(String name, Class<V> beanClass) {
    try {
      return Optional.of(getBean(name, beanClass));
    } catch (RuntimeException e) {
      return Optional.empty();
    }
  }

  @Override
  public synchronized void scanPackage(String name, PackageScanner loader) {
    Set<Class<?>> classesInPackage = loader.findAllClassesIn(name);
    Set<Class<?>> beans =
        classesInPackage.stream()
            .parallel()
            .unordered()
            .filter(c -> c.isAnnotationPresent(Configuration.class))
            .collect(Collectors.toSet());
    for (Class<?> bean : beans) {
      loadConfiguration(bean, loader);
    }
    beans =
        classesInPackage.stream()
            .parallel()
            .unordered()
            .filter(this::isBeanShouldBeLoaded)
            .collect(Collectors.toSet());
    for (Class<?> bean : beans) {
      createBeanMetaInformation(bean);
    }
  }

  @Override
  public synchronized void loadBean(Class<?> beanClass) {
    if (isBeanShouldBeProcessed(beanClass)) {
      createBeanMetaInformation(beanClass);
      loadBeanFrom(beanClass);
      return;
    }
    throw new AnnotationException("No factories found for this class!");
  }

  @Override
  public void loadConfiguration(Class<?> config, PackageScanner scanner)
      throws BeanUndefinedException {
    Method[] methods = config.getDeclaredMethods();
    Arrays.asList(methods).removeIf(m -> !m.isAnnotationPresent(GameBean.class));
    Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
    Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(GameBean.class).order()));
    Object configObj = BasicComponentManager.getComponentCreator().create(config);
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

  @Override
  public synchronized void loadConfiguration(Class<?> configClass) throws BeanUndefinedException {
    loadConfiguration(configClass, new SimplePackageScanner());
  }

  @Override
  public void addBeanFactory(
      BeanFactory gameBeanFactory) {
    this.beanFactories.add(gameBeanFactory);
  }

  @Override
  public void addBeanScanner(BeanScanner beanScanner) {
    this.beanScanners.add(beanScanner);
  }

  @Override
  public String getVariant() {
    return variant;
  }

  private boolean isBeanShouldBeLoaded(Class<?> beanClass) {
    return beanScanners.stream()
        .anyMatch(beanScanner -> beanScanner.shouldBeLoaded(beanClass));
  }

  private boolean isBeanShouldBeProcessed(Class<?> beanClass) {
    return beanFactories.stream()
        .anyMatch(beanFactory -> beanFactory.isShouldBeProcessed(beanClass))
        || gameBeanFactory.isShouldBeProcessed(beanClass);
  }

  private boolean isBeanShouldBeCreated(Class<?> beanClass) {
    return beanFactories.stream()
        .anyMatch(beanFactory -> beanFactory.isShouldBeCreated(beanClass))
        || gameBeanFactory.isShouldBeCreated(beanClass);
  }

  private synchronized void createBeanMetaInformation(Class<?> beanClass) {
    if (!isBeanShouldBeProcessed(beanClass)) {
      return;
    }
    BeanDefinition information = getBeanDataFromClass(beanClass);
    String beanVariant = information.getVariant();
    if (!beanVariant.equals(variant) && !beanVariant.equals(GameBean.GLOBAL_VARIANT)) {
      return;
    }
    String beanName = information.getName();
    saveBeanMetaInformation(beanName, information);
  }

  private synchronized void loadBeanFrom(Class<?> beanClass) {
    viewedMethods.clear();
    createBeanFromClass(beanClass);
  }

  private void saveBeanMetaInformation(String name, BeanDefinition metaInformation) {
    BeanKey beanKey = new BeanKey(name, metaInformation.getRealType());
    if (beans.containsKey(beanKey)) {
      throw new BeanDuplicationException(
          "GameBean name duplication ("
              + name
              + ") for "
              + metaInformation.getRealType().getName());
    }
    beans.put(beanKey, metaInformation);
    Class<?>[] interfaces = metaInformation.getRealType().getInterfaces();
    for (Class<?> interfaceClass : interfaces) {
      beanKey = new BeanKey(name, interfaceClass);
      if (beans.containsKey(beanKey)) {
        throw new BeanDuplicationException(
            "GameBean name duplication (" + name + ") for " + interfaceClass.getName());
      }
      beans.put(beanKey, metaInformation);
    }
  }

  private void createBeanFromClass(Class<?> beanClass) {
    if (!isBeanShouldBeCreated(beanClass)) {
      return;
    }
    BasicComponentManager.getGameLogger()
        .info("Found bean in " + beanClass.getName() + ". Trying to make bean...");
    BeanDefinition beanDefinitionData = getBeanDataFromClass(beanClass);
    String beanVariant = beanDefinitionData.getVariant();
    if (!beanVariant.equals(variant) && !beanVariant.equals(GameBean.GLOBAL_VARIANT)) {
      return;
    }
    String name = beanDefinitionData.getName();
    setBeanReadyStatus(name, beanClass, false);
    Object bean = BasicComponentManager.getComponentCreator().create(beanClass);
    updateBeanObject(name, beanClass, bean);
    setBeanReadyStatus(name, beanClass, true);
    markBeanAsCreated(name, beanClass);
    BasicComponentManager.getGameLogger()
        .info("GameBean with name " + name + " created for " + beanClass.getName());
    BasicComponentManager.getGameLogger().log("", "");
  }

  private BeanDefinition getBeanDataFromClass(Class<?> beanClass) {
    return beanFactories.stream()
        .filter(beanFactory -> beanFactory.isShouldBeProcessed(beanClass))
        .findAny()
        .map(beanFactory -> beanFactory.createBeanMetaInformationFromClass(beanClass))
        .orElseGet(() -> gameBeanFactory.createBeanMetaInformationFromClass(beanClass));
  }

  private Object updateObjectTypeBean(BeanDefinition beanDefinition) {
    if (!beanDefinition.isClass()) {
      beanDefinition.setMethodArgs(
          BasicComponentManager.getComponentCreator().injectBeansToParameters(
              beanDefinition.getRealType(),
              beanDefinition.getMethod().getParameterTypes(),
              beanDefinition.getMethod().getParameterAnnotations()
          ));
      return BasicComponentManager.getComponentCreator().invoke(
          beanDefinition.getMethod(), beanDefinition.getMethodCaller(),
          beanDefinition.getMethodArgs()
      );
    } else {
      return BasicComponentManager.getComponentCreator().create(beanDefinition.getRealType());
    }
  }

  private void updateRealTypeInterfaces(String name, Class<?> beanClass,
                                        BeanDefinition beanDefinitionData) {
    Class<?>[] interfaces = beanClass.getInterfaces();
    BeanKey beanKey = new BeanKey(name, beanClass);
    beans.putIfAbsent(beanKey, beanDefinitionData);
    for (Class<?> inter : interfaces) {
      beanKey = new BeanKey(name, inter);
      beans.putIfAbsent(beanKey, beanDefinitionData);
    }
  }

  private void setBeanReadyStatus(String name, Class<?> beanClass, boolean status) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    BeanDefinition beanDefinitionData = beans.get(beanKey);
    if (beanDefinitionData == null) {
      return;
    }
    beanDefinitionData.setReady(status);
    updateRealTypeInterfaces(name, beanClass, beanDefinitionData);
  }

  private void updateBeanObject(String name, Class<?> beanClass, Object bean) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    BeanDefinition beanDefinitionData = beans.get(beanKey);
    if (beanDefinitionData == null) {
      return;
    }
    beanDefinitionData.setBean(bean);
    updateRealTypeInterfaces(name, beanClass, beanDefinitionData);
  }

  private void markBeanAsCreated(String name, Class<?> beanClass) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    BeanDefinition beanDefinitionData = beans.get(beanKey);
    if (beanDefinitionData == null) {
      return;
    }
    beanDefinitionData.setCreated(true);
    updateRealTypeInterfaces(name, beanClass, beanDefinitionData);
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
      BasicComponentManager.getGameLogger()
          .info(
              "Found GameBean annotation in "
                  + o.getClass().getName()
                  + " in method "
                  + m.getName()
                  + ". Trying to make bean...");
      String name = annotation.name();
      if (name.isEmpty()) {
        name = m.getName().toLowerCase();
      }
      if (checkIfBeanExists(name, m.getReturnType())) {
        throw new BeanDuplicationException(
            "GameBean name duplication " + name + " in " + o.getClass().getName());
      }
      BeanDefinition beanDefinitionData = invoke(m, o);
      viewedMethods.add(m);
      beanDefinitionData.setCreationPolicy(annotation.policy());
      beanDefinitionData.setMethod(m);
      beanDefinitionData.setMethodCaller(o);
      beanDefinitionData.setRealType(beanDefinitionData.getBean().getClass());
      updateRealTypeInterfaces(name, beanDefinitionData.getRealType(), beanDefinitionData);
      BasicComponentManager.getGameLogger()
          .info(
              "GameBean with name "
                  + name
                  + " created for "
                  + beanDefinitionData.getBean().getClass().getName()
                  + " from method "
                  + m.getName());
      BasicComponentManager.getGameLogger().log("", "");
    }
  }

  private boolean checkIfBeanExists(String name, Class<?> beanClass) {
    BeanKey beanKey = new BeanKey(name, beanClass);
    BeanDefinition beanDefinition = beans.get(beanKey);
    if (beanDefinition == null) {
      return false;
    }
    Class<?>[] interfaces = beanClass.getInterfaces();
    for (Class<?> inter : interfaces) {
      checkIfBeanExists(name, inter);
    }
    return true;
  }

  private BeanDefinition invoke(Method m, Object obj) throws ArrayIndexOutOfBoundsException {
    GameBean annotation = m.getAnnotation(GameBean.class);
    Object[] args =
        BasicComponentManager.getComponentCreator().injectBeansToParameters(
            m.getReturnType(), m.getParameterTypes(), m.getParameterAnnotations());
    Object methodResult = BasicComponentManager.getComponentCreator().invoke(m, obj, args);
    BeanDefinition beanDefinitionData = new BeanDefinition();
    beanDefinitionData.setBean(methodResult);
    beanDefinitionData.setCreationPolicy(annotation.policy());
    beanDefinitionData.setMethod(m);
    beanDefinitionData.setMethodArgs(args);
    viewedMethods.add(m);
    return beanDefinitionData;
  }

  private void scanPackages(String[] packages, PackageScanner loader) {
    for (String p : packages) {
      scanPackage(p, loader);
    }
  }
}
