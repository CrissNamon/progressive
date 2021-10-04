package ru.danilarassokhin.progressive.component;

import ru.danilarassokhin.progressive.annotation.FromParent;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;


import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Represents game script
 */
public interface GameScript extends Serializable {

    /**
     * Gets parent GameObject
     * @return Parent game object with this script is attached to
     */
    GameObject gameObject();

    /**
     * Sets parent game object
     * @param parent Object to set as parent
     */
    void setGameObject(GameObject parent);

    /**
     * Fills GameScript fields annotated as @FromParent from parent GameObject this script if attached to
     * @throws IllegalAccessException if field is not accessible
     */
    default void wireFields() throws IllegalAccessException {
        Field[] scriptFields = getClass().getDeclaredFields();
        GameObject parent = gameObject();
        if(parent == null) {
            throw new RuntimeException("Could not autowire fields from parent in " + getClass().getName() + ": parent object is not set");
        }
        for(Field f : scriptFields) {
            f.setAccessible(true);
            if(f.isAnnotationPresent(FromParent.class)) {
                if(ComponentAnnotationProcessor.isAnnotationPresent(IsGameScript.class, f.getType())) {
                    f.set(this, gameObject().getGameScript(f.getType().asSubclass(GameScript.class)));
                }else{
                    throw new RuntimeException("Could not autowire field " + f.getName() + " in " + getClass().getName()
                            + "! Only fields of type IsGameScript and annotated with @IsGameScript supported for autowire");
                }
            }
        }
    }

}
