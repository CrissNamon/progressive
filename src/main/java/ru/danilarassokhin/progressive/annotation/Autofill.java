package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Autofill {
    String value() default "";
}
