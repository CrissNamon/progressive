package tech.hiddenproject.progressive.component;

import java.io.*;
import java.lang.reflect.*;
import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.exception.*;
import tech.hiddenproject.progressive.util.*;

/** Represents game script. */
public interface GameScript extends Serializable {

  /** Called automatically on every {@link Game#start()}. */
  void start();

  /** Called automatically on {@link Game#dispose()}. */
  void dispose();

  /**
   * Called automatically on {@link Game#update(long)}.
   *
   * @param delta Time in ms passed since last call
   */
  void update(long delta);

  /** Stops script. Called automatically on {@link Game#stop()} and {@link GameObject#stop()}. */
  void stop();

  /**
   * Sets parent game object.
   *
   * @param parent Object to set as parent
   */
  void setGameObject(GameObject parent);

  /**
   * Fills GameScript fields annotated as @FromParent from parent GameObject this script if attached
   * to.
   *
   * @throws IllegalAccessException if field is not accessible
   */
  default void wireFields() throws IllegalAccessException {
    Field[] scriptFields = getClass().getDeclaredFields();
    GameObject parent = gameObject();
    if (parent == null) {
      throw new GameScriptException(
          "Could not autowire fields from parent in "
              + getClass().getName()
              + ": parent object is not set");
    }
    for (Field f : scriptFields) {
      f.setAccessible(true);
      if (f.isAnnotationPresent(FromParent.class)) {
        if (ComponentAnnotationProcessor.isAnnotationPresent(IsGameScript.class, f.getType())) {
          f.set(this, gameObject().getGameScript(f.getType().asSubclass(GameScript.class)));
        } else {
          throw new GameScriptException(
              "Could not autowire field "
                  + f.getName()
                  + " in "
                  + getClass().getName()
                  + "! Only fields of type IsGameScript and annotated "
                  + "with @IsGameScript supported for autowire");
        }
      }
    }
  }

  /**
   * Gets parent GameObject.
   *
   * @return Parent game object with this script is attached to
   */
  GameObject gameObject();
}
