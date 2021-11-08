package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

/**
 * Marks class or method as bean provider for Dependency Injection container.
 * <p>policy - defines if bean should be recreated before
 * {@link ru.danilarassokhin.progressive.injection.DIContainer#getBean(String, Class)}</p>
 * <p>name - specifies bean name. If not specified, then bean will get method or class name in lower case</p>
 * <p>order - specifies order in which annotated methods will be executed in
 * {@link ru.danilarassokhin.progressive.annotation.Configuration}</p>
 * <p><b>Bean creation strategy from methods:</b></p>
 * <p>Sort methods by parameters count, then sorts them by order(), then invokes methods in sorted order</p>
 * <p><b>Bean creation strategy from classes:</b></p>
 * <p>Creates meta information of bean. Then creates object from bean class.
 * If class has constructor annotated as {@link ru.danilarassokhin.progressive.annotation.Autofill},
 * then inject beans as constructor parameters and creates object.</p>
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameBean {

  GameBeanCreationPolicy policy() default GameBeanCreationPolicy.SINGLETON;

  String name() default "";

  int order() default 0;
}
