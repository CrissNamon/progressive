package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tech.hiddenproject.progressive.proxy.MethodInterceptor;

/**
 * Used in BasicProxyCreator to create proxy class from original class annotated with
 * {@link Proxy}.
 *
 * <p>You must specify {@link MethodInterceptor} class to be used as method interceptor
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {

  Class<? extends MethodInterceptor> value();
}
