package ru.danilarassokhin.progressive.basic.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import ru.danilarassokhin.progressive.annotation.Intercept;
import ru.danilarassokhin.progressive.annotation.Proxy;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.proxy.MethodInterceptor;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

/**
 * Creates proxy classes
 */
public final class BasicProxyGenerator {

  private final static ClassLoadingStrategy DEFAULT_CLASS_LOADING_STRATEGY = ClassLoadingStrategy.Default.WRAPPER;

  private static BasicProxyGenerator INSTANCE;

  private ClassLoadingStrategy classLoadingStrategy;

  /**
   * Returns instance of {@link ru.danilarassokhin.progressive.basic.proxy.BasicProxyGenerator}
   *
   * @return instance of {@link ru.danilarassokhin.progressive.basic.proxy.BasicProxyGenerator}
   */
  public static BasicProxyGenerator getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new BasicProxyGenerator();
    }
    return INSTANCE;
  }

  private BasicProxyGenerator() {
    setClassLoadingStrategy(DEFAULT_CLASS_LOADING_STRATEGY);
  }

  /**
   * Sets {@link net.bytebuddy.dynamic.loading.ClassLoadingStrategy} for proxy generator
   *
   * @param classLoadingStrategy strategy to use for proxy creation
   */
  public void setClassLoadingStrategy(ClassLoadingStrategy classLoadingStrategy) {
    this.classLoadingStrategy = classLoadingStrategy;
  }

  /**
   * Creates proxy class from {@code original} class.
   * <p>{@code original} class must be annotated as {@link ru.danilarassokhin.progressive.annotation.Proxy}
   * with specified {@link ru.danilarassokhin.progressive.proxy.MethodInterceptor}</p>
   * <p>Methods which need to be intercepted must be annotated as {@link ru.danilarassokhin.progressive.annotation.Intercept}</p>
   *
   * @param original Original class to create proxy from
   * @param <V> Object type to create proxy from
   * @return New proxy class of {@code original}
   */
  public <V> Class<V> createProxyClass(Class<V> original) {
    Proxy proxy = ComponentAnnotationProcessor.findAnnotation(original, Proxy.class);
    if(proxy == null) {
      throw new RuntimeException(original.getName() + " must be annotated as @Proxy!");
    }
    MethodInterceptor interceptor = BasicDIContainer.create(proxy.value());
    Class<?> type = new ByteBuddy()
        .subclass(original)
        .method(ElementMatchers.isAnnotatedWith(Intercept.class))
        .intercept(MethodDelegation.to(new BasicProxyInterceptionHandler(interceptor)))
        .make()
        .load(original.getClassLoader(), classLoadingStrategy)
        .getLoaded();
    return (Class<V>) type;
  }

  /**
   * Creates proxy class from {@code original} class and instantiates it.
   * <p>{@code original} class must be annotated as {@link ru.danilarassokhin.progressive.annotation.Proxy}
   * with specified {@link ru.danilarassokhin.progressive.proxy.MethodInterceptor}</p>
   * <p>Methods which need to be intercepted must be annotated as {@link ru.danilarassokhin.progressive.annotation.Intercept}</p>
   *
   * @param original Original class to create proxy from
   * @param <V> Object type to create proxy from
   * @param args Parameters to be used in constructor of {@code original}
   * @return New proxy class instance of {@code original}
   */
  public <V> V createProxy(Class<V> original, Object... args) {
    return BasicDIContainer.create(
        createProxyClass(original),
        args
    );
  }

}
