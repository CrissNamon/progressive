package tech.hiddenproject.progressive.basic;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.basic.manager.*;
import tech.hiddenproject.progressive.basic.util.*;
import tech.hiddenproject.progressive.component.*;
import tech.hiddenproject.progressive.exception.*;
import tech.hiddenproject.progressive.lambda.*;
import tech.hiddenproject.progressive.manager.*;

/** Basic implementation of {@link Game}. */
public final class BasicGame implements Game<GameStateManager<PublisherSubscription, GameState>> {

  private final Map<Long, GameObject> gameObjects;
  private final ScheduledExecutorService scheduler;
  private final AtomicLong idGenerator;
  private final GameStateManager<PublisherSubscription, GameState> stateManager;

  private boolean isStatic;
  private int frameTime;
  private boolean isStarted;
  private long deltaTime;

  private Class<? extends GameObject> gameObjClass;
  private GameAction preStart;
  private GameAction postStart;
  private GameAction preUpdate;
  private GameAction postUpdate;

  public BasicGame(GameStateManager<PublisherSubscription, GameState> gameStateManager) {
    BasicComponentManager.getGameLogger().info("Progressive IoC initialization...\n");
    stateManager = gameStateManager;
    gameObjects = new ConcurrentSkipListMap<>();
    idGenerator = new AtomicLong(0);
    scheduler = Executors.newScheduledThreadPool(4);
    isStarted = false;
    stateManager.setState(GameState.INIT, this);
  }

  public BasicGame() {
    BasicComponentManager.getGameLogger().info("Progressive IoC initialization...\n");
    stateManager = new BasicGameStateManager();
    gameObjects = new ConcurrentSkipListMap<>();
    idGenerator = new AtomicLong(0);
    scheduler = Executors.newScheduledThreadPool(4);
    isStarted = false;
    stateManager.setState(GameState.INIT, this);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <V extends GameObject> V addGameObject() {
    if (!isGameObjectClassSet()) {
      setGameObjectClass(BasicGameObject.class);
    }
    GameObject gameObject =
        BasicComponentCreator.create(gameObjClass, idGenerator.incrementAndGet());
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
  public boolean isGameObjectClassSet() {
    return gameObjClass != null;
  }

  @Override
  public synchronized void start() {
    stateManager.setState(GameState.STARTED, true);
    if (preStart != null) {
      preStart.make();
    }
    callStartInObject();
    isStarted = true;
    if (postStart != null) {
      postStart.make();
    }
    if (!isStatic) {
      scheduler.scheduleAtFixedRate(this::update, frameTime, frameTime, TimeUnit.MILLISECONDS);
    }
    deltaTime = System.currentTimeMillis();
    stateManager.setState(GameState.PLAYING, true);
  }

  @Override
  public synchronized void update(long delta) {
    if (preUpdate != null) {
      preUpdate.make();
    }
    gameObjects.values().stream().parallel().unordered().forEach(o -> o.update(delta));
    if (postUpdate != null) {
      postUpdate.make();
    }
  }

  @Override
  public synchronized void stop() {
    isStarted = false;
    scheduler.shutdownNow();
    gameObjects.values().parallelStream().unordered().forEach(GameObject::stop);
    stateManager.setState(GameState.STOPPED, true);
  }

  @Override
  public synchronized void dispose() {
    if (!isStarted) {
      gameObjects.values().parallelStream().unordered().forEach(GameObject::dispose);
      gameObjects.clear();
    }
  }

  @Override
  public synchronized void setFrameTime(int milliseconds) {
    if (milliseconds < 1) {
      throw new GameException("Frame rate can't be less than 1 millisecond!");
    }
    this.frameTime = milliseconds;
  }

  @Override
  public synchronized void setStatic(boolean isStatic) {
    if (!isStarted) {
      this.isStatic = isStatic;
    }
  }

  @Override
  public void setPreStart(GameAction action) {
    preStart = action;
  }

  @Override
  public void setPostStart(GameAction action) {
    postStart = action;
  }

  @Override
  public void setPreUpdate(GameAction action) {
    preUpdate = action;
  }

  @Override
  public void setPostUpdate(GameAction action) {
    postUpdate = action;
  }

  @Override
  public GameStateManager<PublisherSubscription, GameState> getStateManager() {
    return stateManager;
  }

  private void callStartInObject() {
    gameObjects.values().stream().parallel().unordered().forEach(GameObject::start);
  }

  private void update() {
    long now = System.currentTimeMillis();
    long delta = now - deltaTime;
    deltaTime = now;
    update(delta);
  }
}
