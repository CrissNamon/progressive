package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Specifies bean name to inject into class field or method parameter.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {
  String value();
}
