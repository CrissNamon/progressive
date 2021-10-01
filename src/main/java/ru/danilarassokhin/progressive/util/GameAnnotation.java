package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.Script;

import java.lang.annotation.Annotation;

public enum GameAnnotation {
    IS_GAME_SCRIPT(Script.class)
    ;

    private Class<? extends Annotation> annotation;

    GameAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
