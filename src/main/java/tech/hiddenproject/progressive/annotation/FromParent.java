package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.*;

/** Marks field in GameScript to be filled from some parent GameObject's GameScript. */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FromParent {}
