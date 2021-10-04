package ru.danilarassokhin.progressive.annotation;

import ru.danilarassokhin.progressive.component.GameScript;

import java.lang.annotation.*;

/**
 * Defines required GameScripts for this GameScript
 * <p><font color="orange">Includes {@link ru.danilarassokhin.progressive.annotation.IsGameScript}</p>
 * <br>
 * If lazy is true then only checks GameObject, which tries to add this GameScript, on having required scripts
 * <br>
 * If lazy is false then creates required GameScripts and attaches them to GameObject if they hasn't been attached already
 * <p>Not inherited</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@IsGameScript
public @interface RequiredGameScript {
    Class<? extends GameScript>[] value() default {};
    boolean lazy() default false;
}
