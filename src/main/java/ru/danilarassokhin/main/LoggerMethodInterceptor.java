package ru.danilarassokhin.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.proxy.MethodInterceptor;

/**
 * Example of {@link ru.danilarassokhin.progressive.proxy.MethodInterceptor}
 */
public class LoggerMethodInterceptor implements MethodInterceptor {
  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args) throws InvocationTargetException, IllegalAccessException {
    BasicGameLogger.info("METHOD " + originMethod.getName() + " INVOKED");
    return proxyMethod.invoke(proxy, args);
  }
}
