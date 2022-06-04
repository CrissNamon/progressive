package tech.hiddenproject.example.proxy;

import java.lang.reflect.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.proxy.*;

/** Example of {@link MethodInterceptor}. */
public class LoggerMethodInterceptor implements MethodInterceptor {
  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    BasicComponentManager.getGameLogger().info("METHOD " + originMethod.getName() + " INVOKED");
    return proxyMethod.invoke(proxy, args);
  }
}
