package ru.danilarassokhin.progressive;

/**
 * Represents save manager
 * <br>
 * Allows to save and load story
 * @param <S> Story type
 * @param <T> Save output type
 */
public interface StorySaveManager<S extends Story, T> {

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
    S load(T save);

}
