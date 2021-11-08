package ru.danilarassokhin.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.proxy.MethodInterceptor;

/**
 * Example of {@link ru.danilarassokhin.progressive.proxy.MethodInterceptor}
 */
public class LoggerMethodInterceptor implements MethodInterceptor {
  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args) throws InvocationTargetException, IllegalAccessException {
    BasicComponentManager
        .getGameLogger().info("METHOD " + originMethod.getName() + " INVOKED");
    return proxyMethod.invoke(proxy, args);
  }
}
