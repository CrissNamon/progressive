package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Just visual annotation for methods protected with
 * {@link ru.danilarassokhin.progressive.util.GameSecurityManager}
 * <p><font color="red">Not securing methods! Use GameSecurityManager methods for this</p>
 * <p>Not inherited</p>
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Protected {
  String value() default "";
}
