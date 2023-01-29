package tech.hiddenproject.progressive.manager;

import tech.hiddenproject.progressive.exception.StateMachineException;

/**
 * Interface represents StateMachine which manages state for some payload P.
 *
 * @param <S> States type
 * @param <E> Events type
 * @param <P> Payload type
 * @author Danila Rassokhin
 */
public interface StateMachine<S, E, P> {

  /**
   * Fires event in StateMachine in safe way. If there is no transition for fired event found, then
   * {@link StateMachineException} will be thrown.
   *
   * @param event Event to fire
   * @throws StateMachineException If there is no transition for fired event found
   */
  void fire(E event) throws StateMachineException;

  /**
   * Fires event in StateMachine in unsafe way. If there is no transition for fired event found,
   * then {@link StateMachineException} will be NOT be thrown.
   *
   * @param event Event to fire
   */
  void post(E event);

  /**
   * @return Current state of this machine
   */
  S getState();

  /**
   * @return Payload for this machine
   */
  P getPayload();

}
