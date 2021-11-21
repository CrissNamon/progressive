package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import ru.hiddenproject.progressive.basic.proxy.BasicProxyCreator;
import ru.hiddenproject.progressive.proxy.MethodInterceptor;

/**
 * Used in {@link BasicProxyCreator}
 * to create proxy class from original class annotated with
 * {@link ru.hiddenproject.progressive.annotation.Proxy}.
 * <p>You must specify {@link ru.hiddenproject.progressive.proxy.MethodInterceptor}
 * class to be used as method interceptor</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {
  Class<? extends MethodInterceptor> value();
}
