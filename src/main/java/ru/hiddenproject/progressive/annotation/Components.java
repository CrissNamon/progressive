package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;

/**
 * Point on specific classes DI container must look at.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Components {
  Class<?>[] value();
}
