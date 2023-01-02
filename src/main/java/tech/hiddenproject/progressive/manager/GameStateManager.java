package tech.hiddenproject.progressive.manager;

import tech.hiddenproject.progressive.lambda.GameActionObject;

/**
 * Represents story state manager.
 *
 * @param <S> State type
 */
public interface GameStateManager<I, S> {

  /**
   * Returns current state.
   *
   * @return Current story state
   */
  S getCurrentState();

  /**
   * Sets state in manager.
   *
   * @param state       New state
   * @param <O>         Action param type
   * @param actionParam Param to pass in action of this state
   */
  <O> void setState(S state, O actionParam);

  /**
   * Adds listener to state.
   *
   * @param state  State to add action
   * @param <V>    Action param type
   * @param action Action to add
   */
  <V> I addListener(S state, GameActionObject<V> action);

  /**
   * Removes listener for state change.
   *
   * @param subscription Subscription to remove
   */
  void removeListener(I subscription);
}
