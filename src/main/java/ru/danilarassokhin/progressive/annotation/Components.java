package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Components {
    Class<?>[] value();
}
