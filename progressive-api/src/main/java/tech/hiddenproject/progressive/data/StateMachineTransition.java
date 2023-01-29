package tech.hiddenproject.progressive.data;

import tech.hiddenproject.progressive.basic.lambda.StateMachineAction;
import tech.hiddenproject.progressive.basic.lambda.StateMachineGuard;

/**
 * Represents transition for {@link tech.hiddenproject.progressive.manager.StateMachine}.
 *
 * @author Danila Rassokhin
 */
public class StateMachineTransition<S, E> {

  private S from;

  private S to;

  private E on;

  private StateMachineAction<S, E> action = transition -> {};

  private StateMachineGuard<S, E> guard = transition -> false;

  public S getFrom() {
    return from;
  }

  public void setFrom(S from) {
    this.from = from;
  }

  public S getTo() {
    return to;
  }

  public void setTo(S to) {
    this.to = to;
  }

  public E getOn() {
    return on;
  }

  public void setOn(E on) {
    this.on = on;
  }

  public StateMachineAction<S, E> getAction() {
    return action;
  }

  public void setAction(StateMachineAction<S, E> action) {
    this.action = action;
  }

  public StateMachineGuard<S, E> getGuard() {
    return guard;
  }

  public void setGuard(StateMachineGuard<S, E> guard) {
    this.guard = guard;
  }
}
