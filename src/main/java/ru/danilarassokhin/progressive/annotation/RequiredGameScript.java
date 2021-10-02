package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@IsGameScript
public @interface RequiredGameScript {
    Class<? extends ru.danilarassokhin.progressive.component.GameScript>[] value() default {};
}
