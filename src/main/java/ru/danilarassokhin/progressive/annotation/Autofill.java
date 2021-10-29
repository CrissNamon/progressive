package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Represents field or constructor which can be injected with some value from Dependency Injection container
 * <p><font color="orange">Not inherited</font></p>
 * <p><font color="orange">value - specifies bean name for auto injection in field</font></p>
 * <p><font color="orange">qualifiers - specifies bean names for auto injection as method parameters</font></p>
 * <p><b>Autofill strategy</b></p>
 * <p><b>In fields:</b> if value (bean name) is specified, then searches for bean with given class and name.
 * If field name is not specified, then uses lower case field name as bean name</p>
 * <p><b>In methods and constructor:</b> Auto injects constructor first. Sorts all methods by args count then.
 * If all qualifiers for all parameters are specified, then searches for beans with given names and types.
 * Otherwise searches for any beans of given types. Invokes method after injection</p>
 * <p>If you need to call some method after object creation,
 * then create no-args method and annotated it as @Autofill with no bean names specified</p>
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autofill {
  String value() default "";

  String[] qualifiers() default {};
}
