package ru.danilarassokhin.progressive.util;

import java.util.Arrays;

public interface GameAnnotationProcessor {

    static boolean isAnnotationPresent(GameAnnotation checker, Class<?> gameScriptClass) {
        return Arrays.stream(checker.getAnnotations()).anyMatch(a -> gameScriptClass.isAnnotationPresent(a));
    }

}
