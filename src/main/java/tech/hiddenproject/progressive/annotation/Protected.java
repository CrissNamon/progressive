package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tech.hiddenproject.progressive.manager.GameSecurityManager;

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
