package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import tech.hiddenproject.progressive.basic.proxy.*;
import tech.hiddenproject.progressive.proxy.*;

/**
 * Used in {@link BasicProxyCreator} to create proxy class from original class annotated with {@link
 * Proxy}.
 *
 * <p>You must specify {@link MethodInterceptor} class to be used as method interceptor
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {
  Class<? extends MethodInterceptor> value();
}
