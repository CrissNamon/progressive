package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

/**
 * Marks class or method as bean provider for Dependency Injection container
 * <p><font color="orange">>Not inherited</p>
 * <p>name - specifies bean name. If not specified, then bean will get method or class name in lower case</p>
 * <p>qualifiers - used in bean creation from method. Specifies params to auto inject as method args.
 * Sorts all methods by args count.
 * If all qualifiers for all parameters are specified, then searches for beans with given names and types.
 * Otherwise searches for any beans of given types. Invokes method after injection and makes bean of method's return type</p>
 * <p>strict - specifies if DI container should try to search for bean with any name or create
 * it from args type or throw a RuntimeException otherwise</p>
 * <p>order - specifies order in which annotated methods will be executed</p>
 * <p><b>Bean creating strategy from methods:</b></p>
 * <p>Sort methods, then tries to search existed beans for args injection or try to create them if strict = false</p>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameBean {

  GameBeanCreationPolicy policy() default GameBeanCreationPolicy.SINGLETON;

  String name() default "";

  String[] qualifiers() default {};

  boolean strict() default false;

  int order() default 0;
}
