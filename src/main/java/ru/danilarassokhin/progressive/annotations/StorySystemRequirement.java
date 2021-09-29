package ru.danilarassokhin.progressive.annotations;

import ru.danilarassokhin.progressive.system.StorySystem;

import java.lang.annotation.*;

/**
 * Represents requirement for StorySystem of other StorySystems
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface StorySystemRequirement {
    public Class<? extends StorySystem>[] value() default {};
}
