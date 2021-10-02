package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Asks Dependency Injection container to scan packages for beans
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public@interface ComponentScan {
    String[] value();
}
