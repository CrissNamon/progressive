package ru.danilarassokhin.progressive.managers;

import ru.danilarassokhin.progressive.lambdas.StoryActionParam;
import ru.danilarassokhin.progressive.data.StoryState;

import java.util.List;

/**
 * Represents story state manager
 * @param <S> State type
 */
public interface StoryStateManager<S extends StoryState> {

    /**
     * @return Current story state
     */
    S getCurrentState();

    /**
     * Sets state in manager
     * @param state New state
     */
    <O> void setState(S state, O actionParam);

    /**
     * @param state State to search
     * @return List with actions for {@code state}
     */
    List<StoryActionParam> getActions(S state);

    /**
     * Adds action to state
     * @param state State to add action
     * @param action Action to add
     */
    <V> void addAction(S state, StoryActionParam<V> action);


}
