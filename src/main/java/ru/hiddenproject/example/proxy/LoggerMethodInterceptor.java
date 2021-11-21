package ru.hiddenproject.example.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.hiddenproject.progressive.basic.BasicComponentManager;
import ru.hiddenproject.progressive.proxy.MethodInterceptor;

/**
 * Example of {@link ru.hiddenproject.progressive.proxy.MethodInterceptor}
 */
public class LoggerMethodInterceptor implements MethodInterceptor {
  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args) throws InvocationTargetException, IllegalAccessException {
    BasicComponentManager
        .getGameLogger().info("METHOD " + originMethod.getName() + " INVOKED");
    return proxyMethod.invoke(proxy, args);
  }
}
