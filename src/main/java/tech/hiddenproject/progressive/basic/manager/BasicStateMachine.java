package tech.hiddenproject.progressive.basic.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.basic.lambda.StateMachineAction;
import tech.hiddenproject.progressive.basic.lambda.StateMachineGuard;
import tech.hiddenproject.progressive.basic.lambda.StateMachinePersister;
import tech.hiddenproject.progressive.data.StateMachineTransition;
import tech.hiddenproject.progressive.exception.StateMachineException;
import tech.hiddenproject.progressive.manager.StateMachine;

/**
 * Basic implementation of {@link StateMachine}.
 *
 * @param <S> States type
 * @param <E> Events type
 * @param <P> Payload type
 * @author Danila Rassokhin
 */
public class BasicStateMachine<S, E, P> implements StateMachine<S, E, P> {

  private final List<StateMachineTransition<S, E>> stateChart;
  private final StateMachinePersister<S, E, P> persister;
  private final P payload;
  private S currentState;

  private BasicStateMachine(S currentState, List<StateMachineTransition<S, E>> stateChart,
                            StateMachinePersister<S, E, P> persister, P payload) {
    this.currentState = currentState;
    this.stateChart = stateChart;
    this.persister = persister;
    this.payload = payload;
    if (persister != null) {
      this.currentState = persister.getCurrentState(payload);
    }
  }

  public static <S, E, P> StateMachineBuilder<S, E, P> create() {
    return new StateMachineBuilder<>();
  }

  /**
   * {@inheritDoc}. Calls {@link StateMachinePersister#persist(Object, StateMachineTransition)}
   * before state transition. {@link StateMachineTransition#getTo()} will return state, that should
   * be saved.
   */
  @Override
  public void fire(E event) {
    List<StateMachineTransition<S, E>> foundTransitions = getTransitionsForEvent(event);
    if (foundTransitions.size() == 0) {
      throw new StateMachineException("There is no transition for event " + event + " found!");
    }
    StateMachineTransition<S, E> transition = foundTransitions.get(0);
    changeState(transition);
  }

  /**
   * {@inheritDoc}. Calls {@link StateMachinePersister#persist(Object, StateMachineTransition)}
   * before state transition. {@link StateMachineTransition#getTo()} will return state, that should
   * be saved.
   */
  @Override
  public void post(E event) {
    List<StateMachineTransition<S, E>> foundTransitions = getTransitionsForEvent(event);
    if (foundTransitions.size() == 1) {
      StateMachineTransition<S, E> transition = foundTransitions.get(0);
      changeState(transition);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public S getState() {
    return currentState;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public P getPayload() {
    return payload;
  }

  private List<StateMachineTransition<S, E>> getTransitionsForEvent(E event) {
    List<StateMachineTransition<S, E>> foundTransitions = stateChart.stream()
        .filter(transition -> transition.getFrom().equals(currentState)
            && transition.getOn().equals(event))
        .collect(Collectors.toList());
    if (foundTransitions.size() > 1) {
      throw new StateMachineException(
          "There is more than one transitions for event " + event + " found");
    }
    return foundTransitions;
  }

  private void changeState(StateMachineTransition<S, E> transition) {
    if (transition.getGuard().test(transition)) {
      throw new StateMachineException("Guard is triggered: " + transition.getGuard().getName());
    }
    if (persister != null) {
      persister.persist(payload, transition);
    }
    transition.getAction().make(transition);
    currentState = transition.getTo();
  }

  public static class StateMachineBuilder<S, E, P> {

    private final List<StateMachineTransition<S, E>> stateChart = new ArrayList<>();

    private StateMachineTransition<S, E> currentTransition;

    private StateMachinePersister<S, E, P> persister;

    private P payload;

    /**
     * Creates new {@link StateMachineTransition}.
     *
     * @param from Source state
     * @param to   Target state
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> transition(S from, S to) {
      currentTransition = new StateMachineTransition<>();
      currentTransition.setFrom(from);
      currentTransition.setTo(to);
      return this;
    }

    /**
     * Adds event for current {@link StateMachineTransition}.
     *
     * @param event Event to change state on
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> event(E event) {
      currentTransition.setOn(event);
      return this;
    }

    /**
     * Add action to make on successful transition.
     *
     * @param action {@link StateMachineAction}
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> action(StateMachineAction<S, E> action) {
      if (currentTransition == null) {
        throw new StateMachineException("Current transition is null");
      }
      currentTransition.setAction(action);
      return this;
    }

    /**
     * Adds guard predicate to check before transition.
     *
     * @param guard {@link StateMachineGuard}
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> guard(StateMachineGuard<S, E> guard) {
      if (currentTransition == null) {
        throw new StateMachineException("Current transition is null");
      }
      currentTransition.setGuard(guard);
      return this;
    }

    /**
     * {@link StateMachinePersister#getCurrentState(Object)} will be called on
     * {@link StateMachineBuilder#build(Object)}.
     * {@link StateMachinePersister#persist(Object, StateMachineTransition)} will be called on
     * success state transition.
     *
     * @param persister {@link StateMachinePersister} to use
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> withPersister(StateMachinePersister<S, E, P> persister) {
      this.persister = persister;
      return this;
    }

    /**
     * Sets payload object to synchronize {@link StateMachine} on.
     *
     * @param payload Payload
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> forPayload(P payload) {
      this.payload = payload;
      return this;
    }

    /**
     * Saves current {@link StateMachineTransition} and creates new one.
     *
     * @return {@link StateMachineBuilder}
     */
    public StateMachineBuilder<S, E, P> and() {
      if (currentTransition == null) {
        throw new StateMachineException("and() called before transition creation!");
      }
      if (currentTransition.getOn() == null || currentTransition.getTo() == null
          || currentTransition.getFrom() == null) {
        throw new StateMachineException("Transition has not been specified correctly!");
      }
      stateChart.add(currentTransition);
      currentTransition = null;
      return this;
    }

    /**
     * Builds {@link StateMachine}.
     *
     * @param initialState Initial state of machine
     * @return {@link StateMachine}
     */
    public StateMachine<S, E, P> build(S initialState) {
      and();
      return new BasicStateMachine<>(initialState, stateChart, persister, payload);
    }
  }
}
