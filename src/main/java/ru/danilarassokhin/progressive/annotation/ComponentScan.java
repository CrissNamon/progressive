package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Asks Dependency Injection container to scan packages for beans
 * <p><font color="orange">Not inherited</p>
 * <p>value - array of package names to scan for @GameBean annotated classes</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {
    String[] value();
}
