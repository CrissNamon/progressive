package tech.hiddenproject.progressive.proxy;

import java.lang.reflect.*;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.basic.proxy.*;

/** Represents method interceptor for {@link BasicProxyCreator}, {@link Proxy}. */
public interface MethodInterceptor {
  Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException;
}
