package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Used in {@link ru.danilarassokhin.progressive.basic.proxy.BasicProxyGenerator}
 * while proxy class creation from original class annotated with
 * {@link ru.danilarassokhin.progressive.annotation.Proxy}.
 * <p>Only methods of original class annotated with {@link ru.danilarassokhin.progressive.annotation.Intercept}
 * will be intercepted</p>
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Intercept {
}
