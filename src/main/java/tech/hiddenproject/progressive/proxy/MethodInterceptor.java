package tech.hiddenproject.progressive.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.basic.proxy.BasicProxyCreator;

/**
 * Represents method interceptor for {@link BasicProxyCreator}, {@link Proxy}.
 */
public interface MethodInterceptor {

  Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException;
}
