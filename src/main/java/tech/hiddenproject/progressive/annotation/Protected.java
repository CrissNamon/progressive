package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import tech.hiddenproject.progressive.manager.*;

/**
 * Just visual annotation for methods protected with. {@link GameSecurityManager}
 *
 * <p>Not securing methods! Use GameSecurityManager methods for this
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Protected {
  String value() default "";
}
