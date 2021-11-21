package ru.hiddenproject.progressive.annotation;

import java.lang.annotation.*;

/**
 * Marks class as GameScript for GameObject.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IsGameScript {
}
