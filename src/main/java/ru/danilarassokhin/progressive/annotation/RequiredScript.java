package ru.danilarassokhin.progressive.annotation;

import ru.danilarassokhin.progressive.component.GameScript;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Script
public @interface RequiredScript {
    Class<? extends GameScript>[] value() default {};
}
