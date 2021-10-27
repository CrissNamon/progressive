package ru.danilarassokhin.progressive.proxy;

public interface ProxyCreator {

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
  <V> Class<V> createProxyClass(Class<V> original);

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
  <V> V createProxy(Class<V> original, Object... args);

}
