package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import ru.hiddenproject.progressive.injection.GameBeanCreationPolicy;

/**
 * Marks class or method as bean provider for Dependency Injection container.
 * <p>policy - defines if bean should be recreated before
 * {@link ru.hiddenproject.progressive.injection.DIContainer#getBean(String, Class)}</p>
 * <p>name - specifies bean name.
 * If not specified, then bean will get method or class name in lower case</p>
 * <p>order - specifies order in which annotated methods will be executed in
 * {@link ru.hiddenproject.progressive.annotation.Configuration}</p>
 * <p>variant - specifies variant this bean will be used in.
 * Only beans with variant,
 * which equals to variant specified in BasicDIContainer will be loaded.
 * You can create beans for different use cases, build platforms, etc.</p>
 * <p><b>Bean creation strategy from methods:</b></p>
 * <p>Sort methods by parameters count, then sorts them by order(),
 * then invokes methods in sorted order</p>
 * <p><b>Bean creation strategy from classes:</b></p>
 * <p>Creates meta information of bean. Then creates object from bean class.
 * If class has constructor annotated as {@link ru.hiddenproject.progressive.annotation.Autofill},
 * then inject beans as constructor parameters and creates object.</p>
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameBean {

  /**
   * Used by default in DI Container.
   */
  String DEFAULT_VARIANT = "_DEFAULT";

  /**
   * Used for global beans. Default variant for bean.
   */
  String GLOBAL_VARIANT = "_GLOBAL";

  GameBeanCreationPolicy policy() default GameBeanCreationPolicy.SINGLETON;

  String name() default "";

  int order() default 0;

  String variant() default GLOBAL_VARIANT;
}
