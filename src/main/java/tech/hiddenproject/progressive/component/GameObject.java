package tech.hiddenproject.progressive.component;

import java.util.*;
import tech.hiddenproject.progressive.*;

/** Represents abstract GameObject. */
public interface GameObject extends GameComponent {

  /**
   * Returns existed GameScript attached to this object or creates new.
   *
   * @param gameScriptClass GameScript class to get
   * @param <V> GameScript to return
   * @param args Arguments to use in script's constructor
   * @return Game script
   */
  <V extends GameScript> V getGameScript(Class<V> gameScriptClass, Object... args);

  /**
   * Checks if this object has given GameScript attached to this object.
   *
   * @param gameScriptClass GameScript class to get
   * @param <V> GameScript type
   * @return true if given script is attached
   */
  <V extends GameScript> boolean hasGameScript(Class<V> gameScriptClass);

  /**
   * Removes game script from this object.
   *
   * @param gameScriptClass GameScript class to remove
   * @param <V> GameScript type
   * @return true if script has been removed
   */
  <V extends GameScript> boolean removeGameScript(Class<V> gameScriptClass);

  /**
   * Gets all scripts attached to this object.
   *
   * @return Collection of GameScripts
   */
  Collection<GameScript> getGameScripts();

  /**
   * Calls {@link GameScript#dispose()} on each {@link GameScript} and removes all {@link
   * GameScript}'s from current object.
   */
  void dispose();

  /**
   * Calls {@link GameScript#start()} on each {@link GameScript}. Called automatically on {@link
   * Game#start()}.
   */
  void start();

  /**
   * Calls {@link GameScript#update(long)} on each {@link GameScript}. Called automatically on
   * {@link Game#update(long)}.
   *
   * @param delta Time in ms passed since last call
   */
  void update(long delta);

  /**
   * Calls {@link GameScript#stop()} on each {@link GameScript}. Called automatically on {@link
   * Game#stop()}.
   */
  void stop();
}
