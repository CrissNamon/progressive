package ru.hiddenproject.progressive.manager;


import ru.hiddenproject.progressive.Game;

/**
 * Represents save manager.
 * <br>
 * Allows to save and load story.
 */
public interface GameSaveManager {

  /**
   * Saves story.
   *
   * @param <T> return save type
   * @return Save
   */
  <T> T save();

  /**
   * Loads story from save.
   *
   * @param save Save to load
   * @param <G>  Game class
   * @return Loaded story
   */
  <G extends Game> G load(Object save);

}
