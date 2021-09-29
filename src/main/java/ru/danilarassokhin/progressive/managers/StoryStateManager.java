package ru.danilarassokhin.progressive.managers;

import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.data.StoryState;

import java.util.List;

/**
 * Represents story state manager
 * @param <S> State type
 */
public interface StoryStateManager<S extends StoryState> {

    /**
     * Returns current state
     * @return Current story state
     */
    S getCurrentState();

    /**
     * Sets state in manager
     * @param state New state
     * @param <O> Action param type
     * @param actionParam Param to pass in action of this state
     */
    <O> void setState(S state, O actionParam);

    /**
     * Returns all actions registered in {@code state}
     * @param state State to search
     * @return List with actions for {@code state}
     */
    @Deprecated
    List<StoryActionObject> getActions(S state);

    /**
     * Returns all listeners attached to {@code state}
     * @param state State to search
     * @return List with actions for {@code state}
     */
    List<StoryActionObject> getListeners(S state);

    /**
     * Adds action to state
     * @param state State to add action
     * @param <V> Action param type
     * @param action Action to add
     */
    @Deprecated
    <V> void addAction(S state, StoryActionObject<V> action);

    /**
     * Adds listener to state
     * @param state State to add action
     * @param <V> Action param type
     * @param action Action to add
     */
    <V> void addListener(S state, StoryActionObject<V> action);


}
