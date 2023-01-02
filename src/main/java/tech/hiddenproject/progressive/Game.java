package tech.hiddenproject.progressive;

import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.lambda.GameAction;
import tech.hiddenproject.progressive.manager.GameStateManager;

/**
 * Represents game.
 */
public interface Game<S extends GameStateManager> {

  /**
   * Creates an empty game object and returns it.
   *
   * @return Created game object
   */
  <V extends GameObject> V addGameObject();

  /**
   * Removes GameObject from game.
   *
   * @param o GameObject to remove from game
   * @return true if object has been removed successfully
   */
  boolean removeGameObject(GameObject o);

  /**
   * Defines GameObject class for game.
   *
   * @param c GameObject class
   * @return true if class has not been already set
   */
  boolean setGameObjectClass(Class<? extends GameObject> c);

  /**
   * Checks if GameObject class has been defined in game.
   *
   * @return true if class has been defined
   */
  boolean isGameObjectClassSet();

  /**
   * Starts the game.
   */
  void start();

  /**
   * Calls global update.
   *
   * @param deltaTime Time passed since last update call
   */
  void update(long deltaTime);

  /**
   * Stops the game.
   */
  void stop();

  /**
   * Disposes game. Calls {@link GameObject#dispose()} on each GameObject.
   */
  void dispose();

  /**
   * Sets time between {@link #update(long)} method calls.
   *
   * @param milliseconds milliseconds to set
   */
  void setFrameTime(int milliseconds);

  /**
   * Defines if game is static.
   *
   * @param isStatic if true {@link #update(long)} will be called automatically.
   */
  void setStatic(boolean isStatic);

  /**
   * Sets action to execute before start.
   *
   * @param action Action to set
   */
  void setPreStart(GameAction action);

  /**
   * Sets action to execute after start.
   *
   * @param action Action to set
   */
  void setPostStart(GameAction action);

  /**
   * Sets action to execute before every update.
   *
   * @param action Action to set
   */
  void setPreUpdate(GameAction action);

  /**
   * Sets action to execute after every update.
   *
   * @param action Action to set
   */
  void setPostUpdate(GameAction action);

  /**
   * Returns current state manager in game.
   *
   * @return {@link GameStateManager}
   */
  S getStateManager();
}
