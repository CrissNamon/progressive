package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

/**
 * Defines required GameScripts for this GameScript
 * <br>
 * If lazy is true then only checks GameObject, which tries to add this GameScript, on having required scripts
 * <br>
 * If lazy is false then creates required GameScripts and attaches them to GameObject if they hasn't been attached already
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@IsGameScript
public @interface RequiredGameScript {
    Class<? extends ru.danilarassokhin.progressive.component.GameScript>[] value() default {};
    boolean lazy() default false;
}
