package tech.hiddenproject.progressive.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.Intercept;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Creates proxy classes.
 */
public final class BasicProxyCreator implements ProxyCreator {

  private static final ClassLoadingStrategy DEFAULT_CLASS_LOADING_STRATEGY =
      ClassLoadingStrategy.Default.WRAPPER;

  private static BasicProxyCreator INSTANCE;

  static {
    BasicComponentManager.setProxyCreator(BasicProxyCreator.getInstance());
  }

  private ClassLoadingStrategy classLoadingStrategy;

  private BasicProxyCreator() {
    setClassLoadingStrategy(DEFAULT_CLASS_LOADING_STRATEGY);
  }

  /**
   * Returns instance of {@link BasicProxyCreator}.
   *
   * @return instance of {@link BasicProxyCreator}
   */

  public static BasicProxyCreator getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BasicProxyCreator();
    }
    return INSTANCE;
  }

  /**
   * Sets {@link ClassLoadingStrategy} for proxy generator.
   *
   * @param classLoadingStrategy strategy to use for proxy creation
   */
  public synchronized void setClassLoadingStrategy(ClassLoadingStrategy classLoadingStrategy) {
    this.classLoadingStrategy = classLoadingStrategy;
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original, MethodInterceptor interceptor) {
    BasicProxyInterceptionHandler basicProxyInterceptionHandler =
        new BasicProxyInterceptionHandler(interceptor);
    DynamicType.Builder builder = createDynamicType(original);
    ElementMatcher.Junction methodMatcher = ElementMatchers.isDeclaredBy(original);
    if (ComponentAnnotationProcessor.isAnnotationPresent(Proxy.class, original)) {
      methodMatcher = ElementMatchers.isAnnotatedWith(Intercept.class);
    }
    builder = builder
        .method(methodMatcher)
        .intercept(MethodDelegation.to(basicProxyInterceptionHandler))
        .method(ElementMatchers.isDeclaredBy(original))
        .intercept(SuperMethodCall.INSTANCE)
        .method(ElementMatchers.isStatic())
        .intercept(SuperMethodCall.INSTANCE)
        .method(ElementMatchers.isConstructor())
        .intercept(SuperMethodCall.INSTANCE);
    DynamicType.Unloaded<V> unloadedClass = makeProxy(builder);
    Class<?> type =
        loadProxyClass(unloadedClass, original.getClassLoader(), getInstance().classLoadingStrategy)
            .getLoaded();
    return (Class<V>) type;
  }

  @Override
  public <V> V createProxy(Class<V> original, MethodInterceptor interceptor, Object... args) {
    return BasicComponentManager.getComponentCreator()
        .create(createProxyClass(original, interceptor), args);
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original, Object... args) {
    Proxy proxy = ComponentAnnotationProcessor.findAnnotation(original, Proxy.class);
    if (proxy == null || proxy.value() == null) {
      throw new RuntimeException(
          original.getName()
              + " must be annotated as @Proxy or MethodInterceptor must be specified!");
    }
    MethodInterceptor interceptor = BasicComponentManager.getComponentCreator()
        .create(proxy.value(), args);
    return createProxyClass(original, interceptor);
  }

  @Override
  public <V> V createProxy(Class<V> original, Object... args) {
    return BasicComponentManager.getComponentCreator().create(createProxyClass(original), args);
  }

  private <V> DynamicType.Builder<V> createDynamicType(Class<V> original) {
    return new ByteBuddy()
        .subclass(
            original, ConstructorStrategy.Default.IMITATE_SUPER_CLASS.withInheritedAnnotations());
  }

  private <V> DynamicType.Unloaded<V> makeProxy(DynamicType.Builder<V> builder) {
    return builder.make();
  }

  private <V> DynamicType.Loaded<V> loadProxyClass(
      DynamicType.Unloaded<V> unloadedClass,
      ClassLoader classLoader,
      ClassLoadingStrategy classLoadingStrategy) {
    return unloadedClass.load(classLoader, classLoadingStrategy);
  }
}
