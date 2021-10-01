package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GameBean {
    GameBeanCreationPolicy policy() default GameBeanCreationPolicy.SINGLETON;
    String name() default "";
}
