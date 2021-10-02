package ru.danilarassokhin.progressive.component;

import ru.danilarassokhin.progressive.annotation.FromParent;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;


import java.lang.reflect.Field;

public interface GameScript {

    GameObject getParent();

    <O extends GameObject> void setParent(O object);

    default void wireFields() throws IllegalAccessException {
        Field[] scriptFields = getClass().getDeclaredFields();
        GameObject parent = getParent();
        if(parent == null) {
            throw new RuntimeException("Could not autowire fields from parent in " + getClass().getName() + ": parent object is not set");
        }
        for(Field f : scriptFields) {
            f.setAccessible(true);
            if(f.isAnnotationPresent(FromParent.class)) {
                if(ComponentAnnotationProcessor.isAnnotationPresent(IsGameScript.class, f.getType())) {

                    f.set(this, getParent().getGameScript(f.getType().asSubclass(GameScript.class)));
                }else{
                    throw new RuntimeException("Could not autowire field " + f.getName() + " in " + getClass().getName()
                            + "! Only fields of type IsGameScript and annotated with @IsGameScript supported for autowire");
                }
            }
        }
    }

}
