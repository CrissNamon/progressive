package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import ru.hiddenproject.progressive.basic.proxy.BasicProxyCreator;

/**
 * Used in {@link BasicProxyCreator}
 * while proxy class creation from original class annotated with
 * {@link ru.hiddenproject.progressive.annotation.Proxy}.
 * <p>Only methods of original class annotated with {@link ru.hiddenproject.progressive.annotation.Intercept}
 * will be intercepted</p>
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Intercept {
}
