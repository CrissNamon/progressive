package ru.danilarassokhin.progressive.basic.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.danilarassokhin.progressive.annotation.Protected;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.manager.GameStateManager;
import ru.danilarassokhin.progressive.manager.GameSecurityManager;

/**
 * Basic implementation of {@link ru.danilarassokhin.progressive.manager.GameStateManager}.
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
    if (listeners.containsKey(state)) {
      listeners.getOrDefault(state, new ConcurrentLinkedQueue<>()).parallelStream().unordered().forEach(a -> a.make(o));
    } else {
      listeners.put(state, new ConcurrentLinkedQueue<>());
    }
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
