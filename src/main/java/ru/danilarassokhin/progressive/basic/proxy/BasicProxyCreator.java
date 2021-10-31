package ru.danilarassokhin.progressive.basic.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import ru.danilarassokhin.progressive.annotation.Intercept;
import ru.danilarassokhin.progressive.annotation.Proxy;
import ru.danilarassokhin.progressive.basic.BasicDIContainer;
import ru.danilarassokhin.progressive.proxy.MethodInterceptor;
import ru.danilarassokhin.progressive.proxy.ProxyCreator;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

/**
 * Creates proxy classes
 */
public final class BasicProxyCreator implements ProxyCreator {

  private final static ClassLoadingStrategy DEFAULT_CLASS_LOADING_STRATEGY = ClassLoadingStrategy.Default.WRAPPER;

  private static BasicProxyCreator INSTANCE;

  private ClassLoadingStrategy classLoadingStrategy;

  /**
   * Returns instance of {@link BasicProxyCreator}
   *
   * @return instance of {@link BasicProxyCreator}
   */
  public static BasicProxyCreator getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new BasicProxyCreator();
    }
    return INSTANCE;
  }

  private BasicProxyCreator() {
    setClassLoadingStrategy(DEFAULT_CLASS_LOADING_STRATEGY);
  }

  /**
   * Sets {@link net.bytebuddy.dynamic.loading.ClassLoadingStrategy} for proxy generator
   *
   * @param classLoadingStrategy strategy to use for proxy creation
   */
  public synchronized void setClassLoadingStrategy(ClassLoadingStrategy classLoadingStrategy) {
    this.classLoadingStrategy = classLoadingStrategy;
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original, MethodInterceptor interceptor) {
    DynamicType.Unloaded<V> unloadedClass = makeProxy(
        createDynamicType(original)
            .method(ElementMatchers.isDeclaredBy(original))
            .intercept(MethodDelegation.to(new BasicProxyInterceptionHandler(interceptor)))
    );
    Class<?> type = loadProxyClass(unloadedClass, original.getClassLoader(), getInstance().classLoadingStrategy)
        .getLoaded();
    return (Class<V>) type;
  }

  @Override
  public <V> V createProxy(Class<V> original, MethodInterceptor interceptor, Object... args) {
    return BasicDIContainer.create(
        createProxyClass(original, interceptor),
        args
    );
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original) {
    Proxy proxy = ComponentAnnotationProcessor.findAnnotation(original, Proxy.class);
    if(proxy == null) {
      throw new RuntimeException(original.getName() + " must be annotated as @Proxy!");
    }
    MethodInterceptor interceptor = BasicDIContainer.create(proxy.value());
    DynamicType.Unloaded<V> unloadedClass = makeProxy(
        createDynamicType(original)
        .method(ElementMatchers.isAnnotatedWith(Intercept.class))
        .intercept(MethodDelegation.to(new BasicProxyInterceptionHandler(interceptor)))
    );
    Class<?> type = loadProxyClass(unloadedClass, original.getClassLoader(), getInstance().classLoadingStrategy)
        .getLoaded();
    return (Class<V>) type;
  }

  @Override
  public <V> V createProxy(Class<V> original, Object... args) {
    return BasicDIContainer.create(
        createProxyClass(original),
        args
    );
  }

  private <V> DynamicType.Builder<V> createDynamicType(Class<V> original) {
    return new ByteBuddy()
        .subclass(original);
  }

  private <V> DynamicType.Unloaded<V> makeProxy(DynamicType.Builder<V> builder) {
    return builder.make();
  }

  private <V> DynamicType.Loaded<V> loadProxyClass(DynamicType.Unloaded<V> unloadedClass,
                                                   ClassLoader classLoader,
                                                   ClassLoadingStrategy classLoadingStrategy) {
    return unloadedClass.load(classLoader, classLoadingStrategy);
  }

}
