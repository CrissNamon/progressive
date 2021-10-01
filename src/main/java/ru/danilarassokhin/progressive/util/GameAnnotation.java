package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.RequiredScript;
import ru.danilarassokhin.progressive.annotation.Script;

import java.lang.annotation.Annotation;

public enum GameAnnotation {
    IS_GAME_SCRIPT(RequiredScript.class, Script.class)
    ;

    private Class<? extends Annotation>[] annotations;

    @SafeVarargs
    GameAnnotation(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    public Class<? extends Annotation>[] getAnnotations() {
        return annotations;
    }
}
