package tech.hiddenproject.progressive.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import tech.hiddenproject.progressive.Game;
import tech.hiddenproject.progressive.basic.lambda.GameAction;
import tech.hiddenproject.progressive.basic.lambda.StateMachinePersister;
import tech.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import tech.hiddenproject.progressive.basic.manager.BasicGameStateManager;
import tech.hiddenproject.progressive.basic.manager.BasicStateMachine;
import tech.hiddenproject.progressive.basic.util.BasicComponentCreator;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.exception.GameException;
import tech.hiddenproject.progressive.manager.GameEvent;
import tech.hiddenproject.progressive.manager.GameState;
import tech.hiddenproject.progressive.manager.StateMachine;

/**
 * Basic implementation of {@link Game}.
 */
public final class BasicGame implements Game<GameState, GameEvent, BasicGame> {

  private final Map<Long, GameObject> gameObjects;
  private final ScheduledExecutorService scheduler;
  private final AtomicLong idGenerator;
  private final StateMachine<GameState, GameEvent, BasicGame> stateMachine;

  private boolean customStateMachine = false;
  private boolean isStatic;
  private int frameTime;
  private boolean isStarted;
  private long deltaTime;

  private Class<? extends GameObject> gameObjClass;
  private GameAction preStart;
  private GameAction postStart;
  private GameAction preUpdate;
  private GameAction postUpdate;

  {
    BasicComponentManager.getGameLogger().info("Progressive IoC initialization...\n");
    gameObjects = new ConcurrentSkipListMap<>();
    idGenerator = new AtomicLong(0);
    scheduler = Executors.newScheduledThreadPool(4);
    isStarted = false;
    BasicGamePublisher.getInstance().sendTo(
        GAME_PUBLISHER_TOPIC,
        GameEvent.INITIALIZATION.setPayload(this)
    );
  }

  public BasicGame(StateMachinePersister<GameState, GameEvent, BasicGame> stateMachinePersister) {
    stateMachine = buildStateMachine(stateMachinePersister);
    if (!customStateMachine) {
      stateMachine.fire(GameEvent.INITIALIZATION);
    }
  }

  public BasicGame() {
    BasicComponentManager.getGameLogger().info("Progressive IoC initialization...\n");
    stateMachine = buildStateMachine(null);
    if (!customStateMachine) {
      stateMachine.fire(GameEvent.INITIALIZATION);
    }
  }

  public BasicGame(StateMachine<GameState, GameEvent, BasicGame> stateMachine) {
    BasicComponentManager.getGameLogger().info("Progressive IoC initialization...\n");
    this.stateMachine = stateMachine;
    customStateMachine = true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public synchronized <V extends GameObject> V addGameObject() {
    if (!isGameObjectClassSet()) {
      setGameObjectClass(BasicGameObject.class);
    }
    GameObject gameObject =
        BasicComponentCreator.create(gameObjClass, idGenerator.incrementAndGet());
    gameObjects.putIfAbsent(idGenerator.get(), gameObject);
    BasicGamePublisher.getInstance().sendTo(
        GAME_PUBLISHER_TOPIC,
        GameEvent.NEW_OBJECT.setPayload(gameObject)
    );
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
    BasicGamePublisher.getInstance().sendTo(
        GAME_PUBLISHER_TOPIC,
        GameEvent.REMOVE_OBJECT.setPayload(o.getId())
    );
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
    if (!customStateMachine) {
      stateMachine.fire(GameEvent.START);
    }
    BasicGamePublisher.getInstance().sendTo(GAME_PUBLISHER_TOPIC, GameEvent.START);
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
    if (!customStateMachine) {
      stateMachine.fire(GameEvent.PLAY);
    }
    BasicGamePublisher.getInstance().sendTo(GAME_PUBLISHER_TOPIC, GameEvent.PLAY);
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
    if (!customStateMachine) {
      stateMachine.fire(GameEvent.STOP);
    }
    BasicGamePublisher.getInstance().sendTo(GAME_PUBLISHER_TOPIC, GameEvent.STOP.setPayload(this));
  }

  @Override
  public synchronized void dispose() {
    if (!isStarted) {
      gameObjects.values().parallelStream().unordered().forEach(GameObject::dispose);
      gameObjects.clear();
    }
    BasicGamePublisher.getInstance().sendTo(GAME_PUBLISHER_TOPIC, GameEvent.GLOBAL_DISPOSE);
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
  public synchronized void setPreStart(GameAction action) {
    preStart = action;
  }

  @Override
  public synchronized void setPostStart(GameAction action) {
    postStart = action;
  }

  @Override
  public synchronized void setPreUpdate(GameAction action) {
    preUpdate = action;
  }

  @Override
  public synchronized void setPostUpdate(GameAction action) {
    postUpdate = action;
  }

  @Override
  public List<GameObject> getAllGameObjects() {
    return Collections.unmodifiableList(new ArrayList<>(gameObjects.values()));
  }

  @Override
  public BasicGameStateManager getStateManager() {
    return null;
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

  private StateMachine<GameState, GameEvent, BasicGame> buildStateMachine(
      StateMachinePersister<GameState, GameEvent, BasicGame> stateMachinePersister) {
    return BasicStateMachine.<GameState, GameEvent, BasicGame> create()
        .transition(GameState.UNDEFINED, GameState.INIT)
        .event(GameEvent.INITIALIZATION)
        .and()
        .transition(GameState.INIT, GameState.STARTED)
        .event(GameEvent.START)
        .and()
        .transition(GameState.STARTED, GameState.PLAYING)
        .event(GameEvent.PLAY)
        .and()
        .transition(GameState.PLAYING, GameState.STOPPED)
        .event(GameEvent.STOP)
        .and()
        .transition(GameState.STOPPED, GameState.STARTED)
        .event(GameEvent.START)
        .withPersister(stateMachinePersister)
        .forPayload(this)
        .build(GameState.UNDEFINED);
  }
}
