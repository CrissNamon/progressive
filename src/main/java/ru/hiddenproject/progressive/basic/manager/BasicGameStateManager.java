package ru.hiddenproject.progressive.basic.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.hiddenproject.progressive.annotation.Protected;
import ru.hiddenproject.progressive.basic.BasicGame;
import ru.hiddenproject.progressive.lambda.GameActionObject;
import ru.hiddenproject.progressive.manager.GameState;
import ru.hiddenproject.progressive.manager.GameStateManager;
import ru.hiddenproject.progressive.manager.GameSecurityManager;

/**
 * Basic implementation of {@link ru.hiddenproject.progressive.manager.GameStateManager}.
 */
public class BasicGameStateManager implements GameStateManager<GameState> {
  private static transient BasicGameStateManager INSTANCE;
  private GameState state;
  private final transient Map<GameState, Queue<GameActionObject>> listeners;

  private BasicGameStateManager() {
    listeners = new ConcurrentHashMap<>();
    setState(GameState.UNDEFINED, null);
  }

  public static BasicGameStateManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BasicGameStateManager();
    }
    return INSTANCE;
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
    Queue<GameActionObject> stateListeners =
        listeners.getOrDefault(state, new ConcurrentLinkedQueue<>());
    stateListeners.stream().parallel().unordered()
        .forEach(a -> a.make(o));
    this.state = state;
  }

  @Override
  public Queue<GameActionObject> getListeners(GameState state) {
    return listeners.getOrDefault(state, new ConcurrentLinkedQueue<>());
  }

  @Override
  public <V> void addListener(GameState state, GameActionObject<V> action) {
    listeners.putIfAbsent(state, new ConcurrentLinkedQueue<>());
    listeners.get(state).add(action);
  }

}
