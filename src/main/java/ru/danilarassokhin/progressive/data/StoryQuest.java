package ru.danilarassokhin.progressive.data;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;

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
     * Sets quest name
     * @param name new name
     */
    void setName(String name);

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
     * Sets on complete action
     * @param onComplete action to be called on quest complete
     */
    void setOnComplete(StoryActionObject onComplete);

    /**
     * Sets complete condition
     * @param completeCondition condition to set
     */
    void setCompleteCondition(StoryCondition completeCondition);

    /**
     * @return true if quest can be done many times
     */
    boolean isUnique();

    /**
     * Sets quest unique param
     * @param unique
     */
    void setUnique(boolean unique);

}
