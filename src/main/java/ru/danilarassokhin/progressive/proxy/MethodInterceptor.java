package ru.danilarassokhin.progressive.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.danilarassokhin.progressive.basic.proxy.BasicProxyGenerator;

/**
 * Represents method interceptor for {@link BasicProxyGenerator},
 * {@link ru.danilarassokhin.progressive.annotation.Proxy}
 */
public interface MethodInterceptor {
  Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args) throws InvocationTargetException, IllegalAccessException;
}
