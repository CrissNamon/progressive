package ru.danilarassokhin.progressive;

/**
 * Represents story quest
 * @param <I> Quest id type
 */
public interface StoryQuest<I> {

    /**
     * @return Quest id
     */
    I getId();

    /**
     * @return Quest name
     */
    String getName();

    /**
     * @return true if quest can be completed
     */
    boolean isCompleted();

    /**
     * Tries to complete quest
     * @return true if quest has been completed
     */
    boolean complete();

    /**
     * @return true if quest can be done many times
     */
    boolean isUnique();

}
