package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;
import ru.danilarassokhin.progressive.proxy.MethodInterceptor;

/**
 * Used in {@link ru.danilarassokhin.progressive.basic.proxy.BasicProxyGenerator}
 * to create proxy class from original class annotated with
 * {@link ru.danilarassokhin.progressive.annotation.Proxy}.
 * <p>You must specify {@link ru.danilarassokhin.progressive.proxy.MethodInterceptor}
 * class to be used as method interceptor</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {
  Class<? extends MethodInterceptor> value();
}
