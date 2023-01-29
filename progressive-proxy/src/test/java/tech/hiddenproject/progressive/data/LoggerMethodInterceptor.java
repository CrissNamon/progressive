package tech.hiddenproject.progressive.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import tech.hiddenproject.progressive.proxy.MethodInterceptor;

/**
 * @author Danila Rassokhin
 */
public class LoggerMethodInterceptor implements MethodInterceptor {

  @Override
  public Object intercept(Method proxyMethod, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return proxyMethod.invoke(proxy, args);
  }
}
