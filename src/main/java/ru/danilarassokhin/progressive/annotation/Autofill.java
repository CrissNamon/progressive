package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Represents field or constructor which can be injected with some value from Dependency Injection container
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Autofill {
    String value() default "";
}
