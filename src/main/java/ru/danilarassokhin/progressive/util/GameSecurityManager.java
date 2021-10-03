package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.lambda.GameCondition;

import java.util.Arrays;

public interface GameSecurityManager {

    static void allowAccessTo(String deniedMessage, Class... classes) {
        try {
            if(!Arrays.asList(classes).contains(getCallerClass())) {
               throw new RuntimeException(deniedMessage);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void denyAccessTo(String deniedMessage, Class... classes) {
        try {
            if(Arrays.asList(classes).contains(getCallerClass())) {
                throw new RuntimeException(deniedMessage);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void denyAccessToAllExtends(String deniedMessage, Class... extendables) {
        try {
            if(Arrays.asList(extendables).contains(getCallerClass().getSuperclass())) {
                throw new RuntimeException(deniedMessage);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void allowAccessToAllExtends(String deniedMessage, Class... extendables) {
        try {
            if(!Arrays.asList(extendables).contains(getCallerClass().getSuperclass())) {
                throw new RuntimeException(deniedMessage);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void allowAccessTo(String deniedMessage, GameCondition processor) {
        if(!processor.isTrue()) {
            throw new RuntimeException(deniedMessage);
        }
    }

    static void denyAccessTo(String deniedMessage, GameCondition processor) {
        if(processor.isTrue()) {
            throw new RuntimeException(deniedMessage);
        }
    }

    static Class getCallerClass() throws ClassNotFoundException {
        String caller = new Exception().getStackTrace()[3].getClassName();
        return Class.forName(caller);
    }

}
