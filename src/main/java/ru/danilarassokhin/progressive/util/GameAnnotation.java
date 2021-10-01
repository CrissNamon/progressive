package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.annotation.isGameScript;

import java.lang.annotation.Annotation;

public enum GameAnnotation {
    IS_GAME_SCRIPT(isGameScript.class),
    IS_GAME_BEAN(GameBean.class)
    ;

    private Class<? extends Annotation> annotation;

    GameAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
