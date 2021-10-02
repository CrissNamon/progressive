package ru.danilarassokhin.progressive.manager;


import ru.danilarassokhin.progressive.Game;

/**
 * Represents save manager
 * <br>
 * Allows to save and load story
 */
public interface GameSaveManager {

    /**
     * Saves story
     * @return Save
     */
    <T> T save();

    /**
     * Loads story from save
     * @param save Save to load
     * @return Loaded story
     */
    <G extends Game> G load(Object save);

}
