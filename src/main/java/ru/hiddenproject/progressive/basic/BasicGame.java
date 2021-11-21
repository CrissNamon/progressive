package ru.hiddenproject.progressive.basic;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import ru.hiddenproject.progressive.Game;
import ru.hiddenproject.progressive.basic.manager.BasicGameStateManager;
import ru.hiddenproject.progressive.basic.util.BasicComponentCreator;
import ru.hiddenproject.progressive.component.GameObject;
import ru.hiddenproject.progressive.exception.GameException;
import ru.hiddenproject.progressive.manager.GameState;

/**
 * Basic implementation of {@link ru.hiddenproject.progressive.Game}.
 */
public final class BasicGame implements Game {

  private final AtomicLong idGenerator;

  private boolean isStatic;
  private int frameTime;

  private final Map<Long, GameObject> gameObjects;
  private final BasicGameStateManager stateManager;
  private Class<? extends GameObject> gameObjClass;
  private final ScheduledExecutorService scheduler;

  private boolean isStarted;
  private long deltaTime;

  public BasicGame() {
    BasicComponentManager
        .getGameLogger().info("Progressive IoC initialization...\n");
    gameObjects = new ConcurrentSkipListMap<>();
    idGenerator = new AtomicLong(0);
    stateManager = BasicGameStateManager.getInstance();
    scheduler = Executors.newScheduledThreadPool(4);
    isStarted = false;
    stateManager.setState(GameState.INIT, this);
  }

  @Override
  public synchronized void start() {
    stateManager.setState(GameState.STARTED, true);
    callStartInObject();
    isStarted = true;
    if (!isStatic) {
      scheduler.scheduleAtFixedRate(this::update, frameTime, frameTime, TimeUnit.MILLISECONDS);
    }
    deltaTime = System.currentTimeMillis();
    stateManager.setState(GameState.PLAYING, true);
  }

  @Override
  public synchronized void update(long delta) {
    gameObjects.values()
        .stream().parallel().unordered()
        .forEach(o -> o.update(delta));
  }

  @Override
  public synchronized void stop() {
    stateManager.setState(GameState.STOPPED, true);
    isStarted = false;
    scheduler.shutdownNow();
  }

  @Override
  public synchronized void dispose() {
    if (!isStarted) {
      gameObjects.values()
          .parallelStream().unordered()
          .forEach(GameObject::dispose);
      gameObjects.clear();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <V extends GameObject> V addGameObject() {
    if (!isGameObjectClassSet()) {
      setGameObjectClass(BasicGameObject.class);
    }
    GameObject gameObject = BasicComponentCreator.create(gameObjClass, idGenerator.incrementAndGet());
    gameObjects.putIfAbsent(idGenerator.get(), gameObject);
    return (V) gameObject;
  }

  @Override
  public synchronized boolean removeGameObject(GameObject o) {
    if (!gameObjects.containsKey(o.getId())) {
      return false;
    }
    GameObject gameObject = gameObjects.get(o.getId());
    gameObject.dispose();
    gameObjects.remove(o.getId());
    return true;
  }

  @Override
  public synchronized boolean setGameObjectClass(Class<? extends GameObject> c) {
    if (gameObjClass != null) {
      return false;
    }
    gameObjClass = c;
    return true;
  }

  @Override
  public synchronized void setFrameTime(int milliseconds) {
    if (milliseconds < 1) {
      throw new GameException("Frame rate can't be less than 1 millisecond!");
    }
    this.frameTime = milliseconds;
  }

  @Override
  public boolean isGameObjectClassSet() {
    return gameObjClass != null;
  }

  @Override
  public synchronized void setStatic(boolean isStatic) {
    if (!isStarted) {
      this.isStatic = isStatic;
    }
  }

  private void callStartInObject() {
    gameObjects.values()
        .stream().parallel().unordered()
        .forEach(GameObject::start);
  }

  private void update() {
    long now = System.currentTimeMillis();
    long delta = now - deltaTime;
    deltaTime = now;
    update(delta);
  }

}
