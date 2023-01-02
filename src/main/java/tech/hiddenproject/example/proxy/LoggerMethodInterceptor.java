package tech.hiddenproject.example.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.proxy.MethodInterceptor;

/**
 * Example of {@link MethodInterceptor}.
 */
public class LoggerMethodInterceptor implements MethodInterceptor {

  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    BasicComponentManager.getGameLogger().info("METHOD " + originMethod.getName() + " INVOKED");
    return proxyMethod.invoke(proxy, args);
  }
}
