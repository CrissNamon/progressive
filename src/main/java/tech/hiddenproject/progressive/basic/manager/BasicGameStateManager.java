package tech.hiddenproject.progressive.basic.manager;

import tech.hiddenproject.progressive.basic.lambda.GameActionObject;
import tech.hiddenproject.progressive.manager.GameState;
import tech.hiddenproject.progressive.manager.GameStateManager;

/**
 * Basic implementation of {@link GameStateManager}.
 *
 * @deprecated Use {@link tech.hiddenproject.progressive.manager.StateMachine} instead.
 */
@Deprecated
public class BasicGameStateManager implements GameStateManager<PublisherSubscription, GameState> {

  private final BasicGamePublisher publisher;

  private GameState state;

  public BasicGameStateManager() {
    publisher = new BasicGamePublisher();
    setState(GameState.UNDEFINED, null);
  }

  @Override
  public GameState getCurrentState() {
    return state;
  }

  @Override
  public synchronized <O> void setState(GameState state, O o) {
    publisher.sendTo(state.name(), o);
    this.state = state;
  }

  @Override
  public <V> PublisherSubscription addListener(GameState state, GameActionObject<V> action) {
    return publisher.subscribeOn(state.name(), action);
  }

  @Override
  public void removeListener(PublisherSubscription subscription) {
    publisher.unsubscribe(subscription);
  }
}
