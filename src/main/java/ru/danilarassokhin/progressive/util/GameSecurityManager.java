package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.lambda.GameCondition;

import java.util.Arrays;

public final class GameSecurityManager {

    public static void allowAccessTo(String deniedMessage, Class... classes) {
        if(!Arrays.asList(classes).contains(getCallerClass())) {
           throw new RuntimeException(deniedMessage);
        }
    }

    public static void denyAccessTo(String deniedMessage, Class... classes) {
        if(Arrays.asList(classes).contains(getCallerClass())) {
            throw new RuntimeException(deniedMessage);
        }
    }

    public static void denyAccessToAllExtends(String deniedMessage, Class... extendables) {
        if(Arrays.asList(extendables).contains(getCallerClass().getSuperclass())) {
            throw new RuntimeException(deniedMessage);
        }
    }

    public static void allowAccessToAllExtends(String deniedMessage, Class... extendables) {
        if(!Arrays.asList(extendables).contains(getCallerClass().getSuperclass())) {
            throw new RuntimeException(deniedMessage);
        }
    }

    public static void allowAccessIf(String deniedMessage, GameCondition processor) {
        if(!processor.isTrue()) {
            throw new RuntimeException(deniedMessage);
        }
    }

    public static void denyAccessIf(String deniedMessage, GameCondition processor) {
        if(processor.isTrue()) {
            throw new RuntimeException(deniedMessage);
        }
    }

    public static Class getCallerClass() {
        try {
            String caller = new Exception().getStackTrace()[3].getClassName();
            return Class.forName(caller);
        }catch (ClassNotFoundException e) {
            return null;
        }
    }

}
