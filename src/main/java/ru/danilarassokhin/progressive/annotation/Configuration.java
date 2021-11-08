package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Used to mark class as bean provider for {@link ru.danilarassokhin.progressive.injection.DIContainer}.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {
}
