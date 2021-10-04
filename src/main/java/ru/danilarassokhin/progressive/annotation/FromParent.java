package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Marks field in GameScript to be filled from some parent GameObject's GameScript
 * <p><font color="orange">Not inherited</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FromParent {
}
