package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tech.hiddenproject.progressive.basic.proxy.BasicProxyCreator;

/**
 * Used in {@link BasicProxyCreator} while proxy class creation from original class annotated with {@link Proxy}.
 *
 * <p>Only methods of original class annotated with {@link Intercept} will be intercepted
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Intercept {

}
