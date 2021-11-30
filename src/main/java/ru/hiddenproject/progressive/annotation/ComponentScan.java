package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;

/**
 * Asks Dependency Injection container to scan packages for beans.
 * <p>value - array of package names to scan for @GameBean annotated classes</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {
  String[] value();
}
