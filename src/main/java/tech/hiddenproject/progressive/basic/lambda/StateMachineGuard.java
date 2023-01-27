package tech.hiddenproject.progressive.basic.lambda;

import tech.hiddenproject.progressive.data.StateMachineTransition;

/**
 * Represents guard predicate for {@link tech.hiddenproject.progressive.manager.StateMachine} which
 * will be evaluated before transition.
 *
 * @author Danila Rassokhin
 */
public interface StateMachineGuard<S, E> {

  /**
   * Predicate to be evaluated before transition. Checks if transition can be done.
   *
   * @param transition Transition to check
   * @return true - if transition can be done
   */
  boolean test(StateMachineTransition<S, E> transition);

  /**
   * @return Guard's name
   */
  default String getName() {
    return "";
  }

}
