package tech.hiddenproject.progressive.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

public final class BasicProxyInterceptionHandler {

  private final MethodInterceptor methodInterceptor;

  BasicProxyInterceptionHandler(MethodInterceptor methodInterceptor) {
    this.methodInterceptor = methodInterceptor;
  }

  private BasicProxyInterceptionHandler() {
    this.methodInterceptor = this::defaultInterceptor;
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

  private Object defaultInterceptor(
      Method method, Method originMethod, Object proxy, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(proxy, args);
  }
}
