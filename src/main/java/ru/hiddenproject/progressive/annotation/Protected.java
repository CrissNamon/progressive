package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import ru.hiddenproject.progressive.manager.GameSecurityManager;

/**
 * Just visual annotation for methods protected with
 * {@link GameSecurityManager}
 * <p><font color="red">Not securing methods! Use GameSecurityManager methods for this</p>
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Protected {
  String value() default "";
}
