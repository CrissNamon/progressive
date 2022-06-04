package tech.hiddenproject.progressive.manager;

import tech.hiddenproject.progressive.*;

/**
 * Represents save manager. <br>
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
   * @param <G> Game class
   * @return Loaded story
   */
  <G extends Game> G load(Object save);
}
