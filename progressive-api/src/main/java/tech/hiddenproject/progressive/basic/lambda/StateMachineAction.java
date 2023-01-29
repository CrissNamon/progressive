package tech.hiddenproject.progressive.basic.lambda;

import tech.hiddenproject.progressive.data.StateMachineTransition;

/**
 * Represents some action for {@link tech.hiddenproject.progressive.manager.StateMachine}, which
 * will be made after transition.
 *
 * @author Danila Rassokhin
 */
public interface StateMachineAction<S, E> {

  void make(StateMachineTransition<S, E> transition);

}
