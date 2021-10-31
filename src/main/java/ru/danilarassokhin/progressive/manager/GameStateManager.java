package ru.danilarassokhin.progressive.manager;

import java.util.List;
import java.util.Queue;
import ru.danilarassokhin.progressive.lambda.GameActionObject;

/**
 * Represents story state manager
 *
 * @param <S> State type
 */
public interface GameStateManager<S extends GameState> {

  /**
   * Returns current state
   *
   * @return Current story state
   */
  S getCurrentState();

  /**
   * Sets state in manager
   *
   * @param state       New state
   * @param <O>         Action param type
   * @param actionParam Param to pass in action of this state
   */
  <O> void setState(S state, O actionParam);

  /**
   * Returns all listeners attached to {@code state}
   *
   * @param state State to search
   * @return Queue with actions for {@code state}
   */
  Queue<GameActionObject> getListeners(S state);


  /**
   * Adds listener to state
   *
   * @param state  State to add action
   * @param <V>    Action param type
   * @param action Action to add
   */
  <V> void addListener(S state, GameActionObject<V> action);


}
