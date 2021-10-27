package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;
import ru.danilarassokhin.progressive.basic.proxy.BasicProxyCreator;

/**
 * Used in {@link BasicProxyCreator}
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
