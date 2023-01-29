package tech.hiddenproject.progressive.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tech.hiddenproject.progressive.annotation.Proxy;

/**
 * Represents method interceptor for BasicProxyCreator, {@link Proxy}.
 */
public interface MethodInterceptor {

  Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException;
}
