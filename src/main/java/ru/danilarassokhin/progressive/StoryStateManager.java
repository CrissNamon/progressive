package ru.danilarassokhin.progressive;

import java.util.List;

/**
 * Represents story state manager
 * @param <S> State type
 * @param <A> State action type
 */
public interface StoryStateManager<S extends StoryState, A extends StoryExtraAction> {

    /**
     * @return Current story state
     */
    S getCurrentState();

    /**
     * Sets state in manager
     * @param state New state
     */
    void setState(S state);

    /**
     * @param state State to search
     * @return List with actions for {@code state}
     */
    List<A> getActions(S state);

    /**
     * Adds action to state
     * @param state State to add action
     * @param action Action to add
     */
    void addAction(S state, A action);

}
