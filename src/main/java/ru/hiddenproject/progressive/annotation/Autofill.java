package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;

/**
 * Represents field or constructor which
 * can be injected with some value from Dependency Injection container.
 * <p><b>Autofill strategy</b></p>
 * <p><b>In fields:</b> if {@link ru.hiddenproject.progressive.annotation.Qualifier}
 * is specified, then searches for bean with given class and name.
 * If field name is not specified, then uses lower case field name as bean name</p>
 * <p><b>In methods and constructor:</b> Auto injects constructor first.
 * If method parameter annotated with {@link ru.hiddenproject.progressive.annotation.Qualifier},
 * then searches for beans with given names and type.
 * Otherwise searches for bean of given type. Invokes method after injection</p>
 * <p>If there are more than one bean of parameter,
 * field type exist and no {@link ru.hiddenproject.progressive.annotation.Qualifier}
 * is specified than throws {@link ru.hiddenproject.progressive.exception.BeanConflictException}</p>
 * <p>If you need to call some method after object creation,
 * then create no-args method and annotate it as @Autofill</p>
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autofill {
}
