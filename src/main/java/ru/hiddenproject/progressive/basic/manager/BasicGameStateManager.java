package ru.hiddenproject.progressive.basic.manager;

import ru.hiddenproject.progressive.annotation.Protected;
import ru.hiddenproject.progressive.basic.BasicGame;
import ru.hiddenproject.progressive.lambda.GameActionObject;
import ru.hiddenproject.progressive.manager.GameSecurityManager;
import ru.hiddenproject.progressive.manager.GameState;
import ru.hiddenproject.progressive.manager.GameStateManager;

/**
 * Basic implementation of {@link ru.hiddenproject.progressive.manager.GameStateManager}.
 */
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
  @Protected("This method is secured. Only game class can call it")
  public synchronized <O> void setState(GameState state, O o) {
    GameSecurityManager.allowAccessTo(
        "This method can be called only from Game class. "
            + "Access denied",
        BasicGame.class, BasicGameStateManager.class);
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
