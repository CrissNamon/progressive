package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Asks Dependency Injection container to scan packages for beans.
 *
 * <p>value - array of package names to scan for @GameBean annotated classes
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

  String[] value();
}
