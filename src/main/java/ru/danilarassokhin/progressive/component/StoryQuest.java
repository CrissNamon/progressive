package ru.danilarassokhin.progressive.component;

import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.lambdas.StoryCondition;

/**
 * Represents story quest
 * @param <I> Quest id type
 */
public interface StoryQuest<I, Q> extends StoryComponent<I>{

    /**
     * Returns quest id
     * @return Quest id
     */
    I getId();

    /**
     * Returns quest name
     * @return Quest name
     */
    String getName();

    /**
     * Sets quest name
     * @param name new name
     */
    void setName(String name);

    /**
     * Returns quest state
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
    void setOnComplete(StoryActionObject<Q> onComplete);

    /**
     * Sets complete condition
     * @param completeCondition condition to set
     */
    void setCompleteCondition(StoryCondition completeCondition);

    /**
     * Returns quest uniqueness
     * @return true if quest can be done many times
     */
    boolean isUnique();

    /**
     * Sets quest unique param
     * @param unique Define if quest is unique (one-time)
     */
    void setUnique(boolean unique);

}
