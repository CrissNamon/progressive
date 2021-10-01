package ru.danilarassokhin.progressive.manager;


import ru.danilarassokhin.progressive.Game;

/**
 * Represents save manager
 * <br>
 * Allows to save and load story
 * @param <S> Story type
 * @param <T> Save output type
 */
public interface GameSaveManager<G extends Game, T> {

    /**
     * Saves story
     * @return Save
     */
    T save();

    /**
     * Loads story from save
     * @param save Save to load
     * @return Loaded story
     */
    G load(T save);

}
