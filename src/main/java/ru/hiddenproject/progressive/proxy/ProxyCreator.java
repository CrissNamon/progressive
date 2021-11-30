package ru.hiddenproject.progressive.proxy;

/**
 * Creates proxy classes and objects.
 */
public interface ProxyCreator {

  /**
   * Creates proxy class from {@code original} class.
   * All methods of original class will be intercepted with given interceptor
   *
   * @param original    Original class to create proxy from
   * @param <V>         Object type to create proxy from
   * @param interceptor {@link ru.hiddenproject.progressive.proxy.MethodInterceptor} to use
   * @return New proxy class of {@code original}
   */
  <V> Class<V> createProxyClass(Class<V> original, MethodInterceptor interceptor);

  /**
   * Creates proxy class from {@code original} class and instantiates it.
   * All methods of original class will be intercepted with given interceptor
   *
   * @param original    Original class to create proxy from
   * @param <V>         Object type to create proxy from
   * @param interceptor {@link ru.hiddenproject.progressive.proxy.MethodInterceptor} to use
   * @param args        Parameters to be used in constructor of {@code original}
   * @return New proxy class of {@code original}
   */
  <V> V createProxy(Class<V> original, MethodInterceptor interceptor, Object... args);

  /**
   * Creates proxy class from {@code original} class.
   * <p>{@code original} class must be annotated
   * as {@link ru.hiddenproject.progressive.annotation.Proxy}
   * with specified {@link ru.hiddenproject.progressive.proxy.MethodInterceptor}</p>
   * <p>Methods which need to be intercepted must be
   * annotated as {@link ru.hiddenproject.progressive.annotation.Intercept}</p>
   *
   * @param original Original class to create proxy from
   * @param <V>      Object type to create proxy from
   * @return New proxy class of {@code original}
   */
  <V> Class<V> createProxyClass(Class<V> original);

  /**
   * Creates proxy class from {@code original} class and instantiates it.
   * <p>{@code original} class must be annotated
   * as {@link ru.hiddenproject.progressive.annotation.Proxy}
   * with specified {@link ru.hiddenproject.progressive.proxy.MethodInterceptor}</p>
   * <p>Methods which need to be intercepted
   * must be annotated as {@link ru.hiddenproject.progressive.annotation.Intercept}</p>
   *
   * @param original Original class to create proxy from
   * @param <V>      Object type to create proxy from
   * @param args     Parameters to be used in constructor of {@code original}
   * @return New proxy class instance of {@code original}
   */
  <V> V createProxy(Class<V> original, Object... args);

}
