package ru.hiddenproject.progressive.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.hiddenproject.progressive.basic.proxy.BasicProxyCreator;

/**
 * Represents method interceptor for {@link BasicProxyCreator},
 * {@link ru.hiddenproject.progressive.annotation.Proxy}
 */
public interface MethodInterceptor {
  Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args) throws InvocationTargetException, IllegalAccessException;
}
