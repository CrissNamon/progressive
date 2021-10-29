package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Point on specific class DI container must look at
 * <p><font color="orange">Not inherited</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Components {
  Class<?>[] value();
}
