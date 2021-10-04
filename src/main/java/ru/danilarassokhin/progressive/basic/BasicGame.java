package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.GameTickRateType;
import ru.danilarassokhin.progressive.basic.configuration.TestConfiguration;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.ComponentCreator;
import ru.danilarassokhin.progressive.util.GameSecurityManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class BasicGame implements Game {

    private static BasicGame INSTANCE;
    private long idGenerator;

    private GameTickRateType gameTickRateType;
    private boolean isStatic;
    private int fps;

    private final Map<Long, GameObject> gameObjects;
    private BasicGameStateManager stateManager;
    private Class<? extends GameObject> gameObjClass;
    private final ScheduledExecutorService scheduler;

    private boolean isStarted;

    private long deltaTime;

    private BasicGame() {
        gameTickRateType = GameTickRateType.PARALLEL;
        gameObjects = new HashMap<>();
        idGenerator = 0;
        stateManager = BasicGameStateManager.getInstance();
        scheduler = Executors.newScheduledThreadPool(2);
        isStarted = false;
        stateManager.setState(GameState.INIT, this);
        BasicDIContainer.getInstance().loadConfiguration(TestConfiguration.class);
    }

    public static BasicGame getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGame();
        }
        return INSTANCE;
    }

    public void start() {
        GameSecurityManager.denyAccessIf("Game has been already started!", () -> isStarted);
        BasicGamePublisher.getInstance().sendTo("start", true);
        isStarted = true;
        if(!isStatic) {
            scheduler.scheduleAtFixedRate(this::update, 0, fps, TimeUnit.MILLISECONDS);
        }
        deltaTime = System.currentTimeMillis();
    }

    private void update() {
        long now = System.currentTimeMillis();
        long delta = now - deltaTime;
        deltaTime = now;
        update(delta);
    }

    public void update(long delta) {
        GameSecurityManager.allowAccessIf("Game param isStatic is set to false. Can't update manually!",
                () -> isStatic && isStarted
                        || GameSecurityManager.getCallerClass().equals(BasicGame.class));
        BasicGamePublisher.getInstance().sendTo("update", delta);
    }

    public void stop() throws InterruptedException {
        GameSecurityManager.allowAccessIf("Game hasn't been started!", () -> isStarted);
        isStarted = false;
        scheduler.shutdownNow();
    }

    @Override
    public GameObject addGameObject() {
        if(!isGameObjectClassSet()) {
            throw new RuntimeException("GameObject class has not been set in game! Use setGameObjectClass method in your game");
        }
        GameObject gameObject = ComponentCreator.create(gameObjClass, ++idGenerator);
        gameObjects.putIfAbsent(idGenerator, gameObject);
        return gameObject;
    }

    @Override
    public boolean removeGameObject(GameObject o) {
        return gameObjects.remove(o.getId()) != null;
    }

    @Override
    public boolean setGameObjectClass(Class<? extends GameObject> c) {
        if(gameObjClass != null) {
            return false;
        }
        gameObjClass = c;
        return true;
    }

    public void setFrameRate(int milliseconds) {
        if(milliseconds < 1) {
            throw new RuntimeException("Frame rate can't be less than 1 millisecond!");
        }
        this.fps = milliseconds;
    }

    @Override
    public boolean isGameObjectClassSet() {
        return gameObjClass != null;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public GameTickRateType getGameTickRateType() {
        return gameTickRateType;
    }

    public void setGameTickRateType(GameTickRateType gameTickRateType) {
        this.gameTickRateType = gameTickRateType;
    }
}
