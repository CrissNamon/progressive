package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.component.GameObject;

/**
 * Represents game
 */
public interface Game {

  /**
   * Creates an empty game object and returns it
   *
   * @return Created game object
   */
  GameObject addGameObject();

  /**
   * Removes GameObject from game
   *
   * @param o GameObject to remove from game
   * @return true if object has been removed successfully
   */
  boolean removeGameObject(GameObject o);

  /**
   * Defines GameObject class for game
   *
   * @param c GameObject class
   * @return true if class has not been already set
   */
  boolean setGameObjectClass(Class<? extends GameObject> c);

  /**
   * Checks if GameObject class has been defined in game
   *
   * @return true if class has been defined
   */
  boolean isGameObjectClassSet();


  /**
   * Starts the game
   */
  void start();

  /**
   * Calls global update
   *
   * @param deltaTime Time passed since last update call
   */
  void update(long deltaTime);

  /**
   * Stops the game
   */
  void stop();

}