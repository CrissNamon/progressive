package tech.hiddenproject.progressive.basic.lambda;

import tech.hiddenproject.progressive.data.StateMachineTransition;

/**
 * Represents persister for {@link tech.hiddenproject.progressive.manager.StateMachine}. Persister
 * can save and load state of {@link tech.hiddenproject.progressive.manager.StateMachine}.
 *
 * @author Danila Rassokhin
 */
public interface StateMachinePersister<S, E, P> {

  /**
   * Saves current state of {@link tech.hiddenproject.progressive.manager.StateMachine}.
   *
   * @param payload    Payload to associate state with
   * @param transition Current transition
   */
  void persist(P payload, StateMachineTransition<S, E> transition);

  /**
   * Loads state for given payload.
   *
   * @param payload Payload to get state for
   * @return State for {@link tech.hiddenproject.progressive.manager.StateMachine}
   */
  S getCurrentState(P payload);

}
