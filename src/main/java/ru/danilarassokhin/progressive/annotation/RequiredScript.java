package ru.danilarassokhin.progressive.annotation;

import ru.danilarassokhin.progressive.component.GameScript;

import java.lang.annotation.*;

@Script
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequiredScript {
    Class<? extends GameScript>[] value() default {};
}
