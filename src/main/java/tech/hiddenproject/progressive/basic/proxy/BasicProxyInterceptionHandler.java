package tech.hiddenproject.progressive.basic.proxy;

import java.lang.reflect.*;
import net.bytebuddy.implementation.bind.annotation.*;
import tech.hiddenproject.progressive.proxy.*;

public final class BasicProxyInterceptionHandler {

  private final MethodInterceptor methodInterceptor;

  protected BasicProxyInterceptionHandler(MethodInterceptor methodInterceptor) {
    this.methodInterceptor = methodInterceptor;
  }

  protected BasicProxyInterceptionHandler() {
    this.methodInterceptor = this::defaultInterceptor;
  }

  private Object defaultInterceptor(
      Method method, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(proxy, args);
  }

  @RuntimeType
  public Object intercept(
      @SuperMethod Method proxyMethod,
      @Origin Method originMethod,
      @This Object proxy,
      @AllArguments Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return methodInterceptor.intercept(proxyMethod, originMethod, proxy, args);
  }
}
