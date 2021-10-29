package ru.danilarassokhin.progressive.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.danilarassokhin.progressive.annotation.*;
import ru.danilarassokhin.progressive.basic.injection.Bean;
import ru.danilarassokhin.progressive.basic.proxy.BasicProxyCreator;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.injection.PackageLoader;
import ru.danilarassokhin.progressive.proxy.ProxyCreator;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic DI container implementation
 * <p color="orange">Can't be extended</p>
 */
public final class BasicDIContainer implements DIContainer {

  private static BasicDIContainer INSTANCE;

  private final Map<Class<?>, Map<String, Bean>> beans;
  private final Set<Method> viewedMethods;

  private Class<?> scanFrom;

  private ProxyCreator proxyCreator;

  private BasicDIContainer() {
    BasicGameLogger.getInstance().info("Progressive DI initialization...\n");
    beans = new HashMap<>();
    viewedMethods = new HashSet<>();
    proxyCreator = BasicProxyCreator.getInstance();
  }

  protected static BasicDIContainer createInstance() {
    if(INSTANCE == null) {
      INSTANCE = new BasicDIContainer();
    }
    return INSTANCE;
  }

  public static BasicDIContainer getInstance() {
    if (INSTANCE == null) {
      throw new RuntimeException("DI Container has not been initialized! Call GameStarter.init(String[] args) first!");
    }
    return INSTANCE;
  }

  /**
   * Returns proxy creator used in proxy creation.
   *
   * @return Current {@link ru.danilarassokhin.progressive.proxy.ProxyCreator}
   */
  public ProxyCreator getProxyCreator() {
    return proxyCreator;
  }

  /**
   * Sets {@link ru.danilarassokhin.progressive.proxy.ProxyCreator}
   *  which will be used for proxy creation.
   *
   * @param proxyCreator {@link ru.danilarassokhin.progressive.proxy.ProxyCreator} to use
   */
  public void setProxyCreator(ProxyCreator proxyCreator) {
    this.proxyCreator = proxyCreator;
  }

  private void scanPackage(String name, PackageLoader loader) {
    Set<Class<?>> classesInPackage = loader.findAllClassesIn(name);
    for (Class<?> c : classesInPackage) {
      loadBeanFrom(c);
    }
  }

  /**
   * Creates bean from {@code beanClass}.
   *
   * @param beanClass Class to create bean from
   */
  public void loadBeanFrom(Class<?> beanClass) {
    viewedMethods.clear();
    try {
      GameBean annotation = ComponentAnnotationProcessor
          .findAnnotation(beanClass, GameBean.class);
      if (annotation != null) {
        createBeanFromClass(beanClass);
      } else if (beanClass.getSuperclass().equals(AbstractConfiguration.class)) {
        Method[] methods = beanClass.getDeclaredMethods();
        Arrays.asList(methods).removeIf(m -> !m.isAnnotationPresent(GameBean.class));
        Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
        Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(GameBean.class).order()));
        for (Method method : methods) {
          if (viewedMethods.contains(method)) {
            continue;
          }
          if (method.isAnnotationPresent(GameBean.class)) {
            BasicGameLogger.getInstance().info("Found GameBean annotation in " + beanClass.getName()
                + " in method " + method.getName()
                + ". Trying to make bean...");
            Object bean = create(beanClass);
            createBeanFromMethod(method, bean);
          }
        }
      } else {
        throw new RuntimeException(beanClass.getName()
            + " is not a game bean or configuration");
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException("Something gone wrong while bean creation from "
          + beanClass.getName()
          + "! Exception: " + e.getMessage());
    }
  }

  private void createBeanFromClass(Class<?> beanClass) {
    GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    String name = annotation.name();
    if (name.isEmpty()) {
      name = beanClass.getName().toLowerCase();
    }
    BasicGameLogger.getInstance().info("Found GameBean annotation in "
        + beanClass.getName() + ". Trying to make bean...");
    Object beanObj = null;
    if (annotation.policy().equals(GameBeanCreationPolicy.SINGLETON)) {
      beanObj = create(beanClass);
    }
    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
    Bean beanData = new Bean(beanObj, annotation.policy());
    if (beansOfClass.containsKey(name)) {
      throw new RuntimeException("GameBean name duplication "
          + name + " in " + beanClass.getName());
    }
    beansOfClass.putIfAbsent(name, beanData);
    beans.putIfAbsent(beanClass, beansOfClass);
    BasicGameLogger.getInstance().info("GameBean with name " + name + " created for " + beanClass.getName());
    BasicGameLogger.getInstance().info("");
  }

  private void createBeanFromMethod(Method m, Object o) throws InvocationTargetException,
      IllegalAccessException {
    if(viewedMethods.contains(m)) {
      return;
    }
    m.setAccessible(true);
    if (!m.getReturnType().equals(Void.TYPE)) {
      GameBean annotation = m.getAnnotation(GameBean.class);
      String name = annotation.name();
      if (name.isEmpty()) {
        name = m.getName().toLowerCase();
      }
      Map<String, Bean> beansOfClass = beans.getOrDefault(
          m.getReturnType(), new HashMap<>()
      );
      Bean beanData = invoke(m, o);
      viewedMethods.add(m);
      beanData.setCreationPolicy(annotation.policy());
      beanData.setMethod(m);
      beanData.setMethodCaller(o);
      if (beansOfClass.containsKey(name)) {
        throw new RuntimeException("GameBean name duplication "
            + name + " in " + o.getClass().getName());
      }
      beansOfClass.putIfAbsent(name, beanData);
      beans.putIfAbsent(beanData.getBean().getClass(), beansOfClass);
      BasicGameLogger.getInstance().info("GameBean with name " + name + " created for "
          + beanData.getBean().getClass().getName() + " from method " + m.getName());
      BasicGameLogger.getInstance().info("");
    }
  }

  private Bean invoke(Method m, Object obj) throws InvocationTargetException,
      IllegalAccessException, ArrayIndexOutOfBoundsException {
    Class<?>[] paramTypes = m.getParameterTypes();
    Object[] args = new Object[paramTypes.length];
    GameBean annotation = m.getAnnotation(GameBean.class);
    String[] names = annotation.qualifiers();
    if (names.length != args.length && names.length != 0) {
      throw new RuntimeException("You must set all qualifiers for GameBean method "
          + m.getDeclaringClass().getName() + " "
          + m.getName() + " or doesn't set them at all!");
    }
    boolean hasQualifiers = names.length != 0;
    for (int i = 0; i < paramTypes.length; ++i) {
      if (!hasQualifiers) {
        try {
          args[i] = tryGetBean(paramTypes[i]);
        } catch (BeanNotFoundException e) {
          if (annotation.strict()) {
            throw new RuntimeException(m.getName() + " in " + m.getDeclaringClass().getName()
                + " annotated as strict but no beans found for " + paramTypes[i].getName());
          }
          loadBeanFrom(paramTypes[i]);
          args[i] = getBean(paramTypes[i]);
        }
      } else {
        try {
          args[i] = tryGetBean(names[i], paramTypes[i]);
        } catch (BeanNotFoundException beanNotFoundException) {
          if (annotation.strict()) {
            throw new RuntimeException(m.getName() + " in " + m.getDeclaringClass().getName()
                + " annotated as strict but not beans found for " + paramTypes[i].getName());
          }
          try {
            BasicGameLogger.getInstance().warning("GameBean of class " + paramTypes[i].getName()
                + " not found! Trying to create it from existing method...");
            createBeanFromMethod(obj.getClass().getMethod(names[i]), obj);
          } catch (NoSuchMethodException | ArrayIndexOutOfBoundsException e) {
            loadBeanFrom(paramTypes[i]);
          } finally {
            args[i] = getBean(names[i], paramTypes[i]);
          }
        }
      }
    }
    Object methodResult = invoke(m, obj, args);
    Bean beanData = new Bean(methodResult);
    beanData.setMethod(m);
    beanData.setMethodArgs(args);
    viewedMethods.add(m);
    return beanData;
  }

  private Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
    try {
      InputStream stream = getDefaultClassLoader()
          .getResourceAsStream(packageName.replaceAll("[.]", "/"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      return reader.lines()
          .filter(line -> line.endsWith(".class"))
          .map(line -> getClass(line, packageName))
          .collect(Collectors.toSet());
    } catch (NullPointerException e) {
      throw new RuntimeException("ClassLoader is not available! " +
          "Load configuration through you own PackageLoader " +
          "with loadConfiguration(config, loader)");
    }
  }

  private Class<?> getClass(String className, String packageName) {
    try {
      return Class.forName(packageName + "."
          + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class " + className + " not found!");
    }
  }

  public <V> V getBean(Class<V> beanClass) {
    Map.Entry<String, Bean> bean = beans.get(beanClass).entrySet().stream()
        .findFirst().orElse(null);
    if (bean == null) {
      throw new RuntimeException("There is no beans for "
          + beanClass.getName() + " were found!");
    }
    V exists = (V) bean.getValue().getBean();
    if (bean.getValue().getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
      if (bean.getValue().getMethod() != null) {
        try {
          exists = (V) (invoke(bean.getValue().getMethod(),
              bean.getValue().getMethodCaller(),
              bean.getValue().getMethodArgs()));

        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
      } else {
        exists = create(beanClass);
      }
      bean.getValue().setBean(exists);
      beans.get(beanClass).put(bean.getKey(), bean.getValue());
    }
    return exists;
  }

  /**
   * Gets random bean bean with given class
   *
   * @param beanClass Bean class to find
   * @param <V>       Bean object to return
   * @param name      Bean name to search
   * @return Bean object or throws RuntimeException if bean not found
   * @throws BeanNotFoundException if no beans found for given class
   */
  public <V> V tryGetBean(String name, Class<V> beanClass) throws BeanNotFoundException {
    try {
      return getBean(name, beanClass);
    } catch (Exception e) {
      throw new BeanNotFoundException("Bean with name " + name
          + " and type " + beanClass.getName() + " not found");
    }
  }

  /**
   * Gets random bean bean with given class
   *
   * @param beanClass Bean class to find
   * @param <V>       Bean object to return
   * @return Bean object or throws RuntimeException if bean not found
   * @throws BeanNotFoundException if no beans found for given class
   */
  public <V> V tryGetBean(Class<V> beanClass) throws BeanNotFoundException {
    try {
      return getBean(beanClass);
    } catch (Exception e) {
      throw new BeanNotFoundException("Bean with type "
          + beanClass.getName() + " not found");
    }
  }

  public <V> V getBean(String name, Class<V> beanClass) {
    if (!beans.containsKey(beanClass)) {
      throw new RuntimeException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!");
    }
    Bean b = beans.getOrDefault(beanClass, new HashMap<>()).getOrDefault(name, null);
    if (b == null) {
      throw new RuntimeException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!");
    }
    V exists = (V) b.getBean();
    if (exists == null && b.getCreationPolicy().equals(GameBeanCreationPolicy.SINGLETON)) {
      throw new RuntimeException("GameBean called " + name
          + " for class " + beanClass.getName() + " not found!");
    }
    if (b.getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
      if (b.getMethod() != null) {
        try {
          exists = (V) (invoke(b.getMethod(), b.getMethodCaller(), b.getMethodArgs()));

        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
      } else {
        exists = create(beanClass);
      }
      b.setBean(exists);
      beans.get(beanClass).put(name, b);
    }
    return exists;
  }

  private void scanPackages(String[] packages, PackageLoader loader) {
    scanFrom = this.getClass();
    for (String p : packages) {
      scanPackage(p, loader);
    }
  }

  @Override
  public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config, PackageLoader loader) {
    if (config.isAnnotationPresent(Components.class)) {
      scanFrom = config;
      Components components = config.getAnnotation(Components.class);
      loadBeansFrom(components.value());
    }
    if (config.isAnnotationPresent(ComponentScan.class)) {
      scanFrom = config;
      ComponentScan componentScan = config.getAnnotation(ComponentScan.class);
      scanPackages(componentScan.value(), loader);
    }
    loadBeanFrom(config);
  }

  private void loadBeansFrom(Class<?>[] classes) {
    for (Class<?> c : classes) {
      loadBeanFrom(c);
    }
  }

  /**
   * Loads configuration from class with simple {@link ru.danilarassokhin.progressive.injection.PackageLoader}
   *
   * @param config Configuration class
   * @param <C>    Configuration class type
   */
  public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config) {
    loadConfiguration(config, this::findAllClassesUsingClassLoader);
  }

  private ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = scanFrom.getClassLoader();
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ex) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
      }
    }
    return cl;
  }

  /**
   * Creates object from given class and makes autoinjection for fields and methods if they annotated as @Autofill
   *
   * @param componentClass Object class to instantiate
   * @param args           Parameters to pass in class constructor
   * @param <C>            Object to instantiate
   * @return Instantiated object of null
   */
  public static <C> C create(Class<C> componentClass, Object... args) {
    Proxy proxy = componentClass.getAnnotation(Proxy.class);
    if(proxy != null) {
      componentClass = BasicDIContainer.getInstance()
          .getProxyCreator().createProxyClass(componentClass);
    }
    try {
      Class<?>[] argsTypes = new Class[args.length];
      for (int i = 0; i < args.length; ++i) {
        argsTypes[i] = args[i].getClass();
      }
      C instance = null;
      BasicDIContainer diContainer = BasicDIContainer.getInstance();
      Constructor<?>[] constructors = componentClass.getDeclaredConstructors();
      Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));
      for (Constructor<?> constructor : constructors) {
        if (constructor.isAnnotationPresent(Autofill.class)) {
          args = new Object[constructor.getParameterCount()];
          argsTypes = constructor.getParameterTypes();
          Autofill autofill = constructor.getAnnotation(Autofill.class);
          String[] qualifiers = autofill.qualifiers();
          for (int i = 0; i < constructor.getParameterCount(); ++i) {
            if (qualifiers.length == constructor.getParameterCount()) {
              args[i] = diContainer.getBean(qualifiers[i], argsTypes[i]);
            } else {
              args[i] = diContainer.getBean(argsTypes[i].getName().toLowerCase(), argsTypes[i]);
            }
          }
          constructor.setAccessible(true);
          instance = (C) constructor.newInstance(args);
          break;
        }
      }
      if (instance == null) {
        Constructor<C> constructor = componentClass.getDeclaredConstructor(argsTypes);
        constructor.setAccessible(true);
        instance = constructor.newInstance(args);
      }
      Field[] fields = instance.getClass().getDeclaredFields();
      for (Field f : fields) {
        f.setAccessible(true);
        if (f.isAnnotationPresent(Autofill.class)) {
          Autofill autofill = f.getAnnotation(Autofill.class);
          String name = autofill.value();
          if (name.isEmpty()) {
            name = f.getName().toLowerCase();
          }
          f.set(instance, diContainer.getBean(name, f.getType()));
        }
      }
      Method[] methods = instance.getClass().getDeclaredMethods();
      Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
      for (Method m : methods) {
        m.setAccessible(true);
        if (m.isAnnotationPresent(Autofill.class)) {
          Autofill autofill = m.getAnnotation(Autofill.class);
          String[] names = autofill.qualifiers();
          argsTypes = m.getParameterTypes();
          args = new Object[argsTypes.length];
          for (int i = 0; i < m.getParameterCount(); ++i) {
            try {
              if (names.length != argsTypes.length) {
                args[i] = diContainer.tryGetBean(argsTypes[i]);
              } else {
                args[i] = diContainer.tryGetBean(names[i], argsTypes[i]);
              }
            } catch (BeanNotFoundException e) {
              throw new RuntimeException("Could not Autofill method "
                  + m.getName() + " in " + instance.getClass().getName()
                  + "! Beans for " + argsTypes[i].getName() + " not found...");
            }
          }
          invoke(m, instance, args);
        }
      }
      return instance;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not instantiate component "
          + componentClass.getName() + "! Exception: " + e.getMessage());
    } catch (NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not instantiate component "
          + componentClass.getName() + "! Component must have such a constructor: " + e.getMessage());
    }
  }

  /**
   * Invokes method from given object and given args
   *
   * @param method Method to invoke
   * @param from   Object to invoke method from
   * @param args   Parameters to invoke method with
   */
  public static Object invoke(Method method, Object from, Object... args) {
    try {
      method.setAccessible(true);
      MethodHandles.Lookup lookup = MethodHandles.lookup();
      List<Object> castedArgs = new ArrayList<>();
      castedArgs.add(from);
      for (Object o : args) {
        castedArgs.add(o);
      }
      if (args.length == 0) {
        return invokeObjectMethod(from, method);
      } else if (args.length == 1) {
        return invokeObjectMethodWithOneParam(from, method, args[0]);
      } else {
        return lookup.unreflect(method).invokeWithArguments(castedArgs);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException("Error while method "
          + method.getName() + " invocation! Exception: " + e.getMessage());
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      throw new RuntimeException("Exception has occurred while method invokation!" +
          " Exception: " + throwable.getMessage());
    }
  }

  private static Object invokeObjectMethod(Object bean, Method method) throws Throwable {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    MethodType invokedType = MethodType.methodType(Function.class);
    method.setAccessible(true);
    MethodType func = caller.unreflect(method).type();
    CallSite site = LambdaMetafactory.metafactory(
        caller,
        "apply",
        invokedType,
        func.generic(),
        caller.unreflect(method),
        MethodType.methodType(Object.class, bean.getClass())
    );
    Function<Object, Object> fullFunction = (Function<Object, Object>) site.getTarget().invokeExact();
    return fullFunction.apply(bean);
  }

  private static Object invokeObjectMethodWithOneParam(Object bean, Method method, Object arg) throws Throwable {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    MethodType invokedType = MethodType.methodType(BiFunction.class);
    method.setAccessible(true);
    MethodType func = caller.unreflect(method).type();
    Class<?>[] methodParamTypes = method.getParameterTypes();
    CallSite site = LambdaMetafactory.metafactory(
        caller,
        "apply",
        invokedType,
        func.generic(),
        caller.unreflect(method),
        MethodType.methodType(Object.class, bean.getClass(), methodParamTypes[0])
    );
    BiFunction<Object, Object, Object> fullFunction = (BiFunction<Object, Object, Object>) site.getTarget().invoke();
    return fullFunction.apply(bean, methodParamTypes[0].cast(arg));
  }

  /**
   * Check if given modifier is included in all modifiers
   * <p>You can check if method or field you got from Reflection have some modifiers like private, public, etc</p>
   *
   * @param allModifiers     All modifiers field or method has
   * @param specificModifier Modifier to check
   * @return true if given modifier presented in all modifiers
   */
  public static boolean isModifierSet(int allModifiers, int specificModifier) {
    return (allModifiers & specificModifier) > 0;
  }
}
