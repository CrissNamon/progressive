package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tech.hiddenproject.progressive.component.GameScript;

/**
 * Defines required GameScripts for this GameScript.
 *
 * <p>Includes {@link IsGameScript} <br>
 * If lazy is true then only checks GameObject, which tries to add this GameScript, on having required scripts <br> If
 * lazy is false then creates required GameScripts and attaches them to GameObject if they hasn't been attached already
 *
 * <p>Not inherited
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@IsGameScript
public @interface RequiredGameScript {

  Class<? extends GameScript>[] value() default {};

  boolean lazy() default false;
}
