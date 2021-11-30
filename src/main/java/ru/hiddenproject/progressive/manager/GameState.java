package ru.hiddenproject.progressive.manager;

/**
 * Represents some game state.
 */
public enum GameState {

  /**
   * Game state is undefined.
   * <p>Default state before game instance creation</p>
   * <p>Returns null in callback</p>
   */
  UNDEFINED,

  /**
   * Indicates that game instance has been created, but game hasn't been started yet.
   * <p>Called only once</p>
   * <p>Returns true in callback</p>
   */
  INIT,

  /**
   * Game's start method called.
   * <p>Returns true in callback</p>
   */
  STARTED,

  /**
   * Game's will be called after this.
   * <p>Returns true in callback</p>
   */
  PLAYING,

  /**
   * Game has been stopped.
   * <p>Returns true in callback</p>
   */
  STOPPED
}
