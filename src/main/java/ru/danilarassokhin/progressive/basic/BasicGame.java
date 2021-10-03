package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.ComponentCreator;
import ru.danilarassokhin.progressive.util.GameSecurityManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class BasicGame implements Game {

    private static BasicGame INSTANCE;
    private final Map<Long, GameObject> gameObjects;
    private final Set<GameObjectWorker> gameObjectWorkers;
    private BasicGameStateManager stateManager;
    private Class<? extends GameObject> gameObjClass;
    private int tick;
    private final ScheduledExecutorService scheduler;
    private boolean isStatic;

    private boolean isStarted;

    private BasicGame() {
        gameObjects = new HashMap<>();
        stateManager = BasicGameStateManager.getInstance();
        gameObjectWorkers = new HashSet<>();
        scheduler = Executors.newScheduledThreadPool(2);
        stateManager.setState(GameState.INIT, this);

        isStarted = false;
    }

    public static BasicGame getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGame();
        }
        return INSTANCE;
    }

    public void start() {
        GameSecurityManager.denyAccessIf("Game has been already started!", () -> isStarted);
        isStarted = true;
        gameObjectWorkers.forEach(w -> ComponentCreator.invoke(w.getStartMethod(), gameObjects.get(w.getGameObjId())));
        if(!isStatic) {
            update();
        }
    }

    public void update() {
        GameSecurityManager.allowAccessIf("Game param isStatic is set to false. Can't update manually!",
                () -> isStatic && isStarted
        || GameSecurityManager.getCallerClass().equals(BasicGame.class));
        gameObjectWorkers.forEach(w -> {
            ScheduledFuture resultFuture = scheduler
                    .scheduleAtFixedRate(() -> ComponentCreator.invoke(w.getUpdateMethod(), gameObjects.get(w.getGameObjId())), 0, tick, TimeUnit.MILLISECONDS);
        });
    }

    public void stop() throws InterruptedException {
        GameSecurityManager.allowAccessIf("Game hasn't been started!", () -> isStarted);
        isStarted = false;
        scheduler.shutdown();
        if(scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
    }

    @Override
    public GameObject addGameObject() {
        if(!isGameObjectClassSet()) {
            throw new RuntimeException("GameObject class has not been set in game! Use setGameObjectClass method in your game");
        }
        try {
            Long lastId = gameObjects.keySet().stream().max(Long::compareTo).orElse(0L);
            GameObject gameObject = ComponentCreator.create(gameObjClass, ++lastId);
            gameObjects.putIfAbsent(lastId, gameObject);
            Method update = gameObject.getClass().getDeclaredMethod("update");
            Method start = gameObject.getClass().getDeclaredMethod("start");
            if(!ComponentCreator.isModifierSet(update.getModifiers(), Modifier.PRIVATE)
                || !ComponentCreator.isModifierSet(start.getModifiers(), Modifier.PRIVATE)) {
                throw new RuntimeException("GameObject must have methods private void update() and private void start() in " + gameObjClass.getName());
            }
            update.setAccessible(true);
            start.setAccessible(true);
            gameObjectWorkers.add(new GameObjectWorker(update, start, lastId));
            return gameObject;
        }catch (NoSuchMethodException e) {
            throw new RuntimeException("GameObject must have methods private void update() and private void start() in " + gameObjClass.getName());
        }
    }

    @Override
    public boolean removeGameObject(GameObject o) {
        if(gameObjects.remove(o.getId()) != null) {
            gameObjectWorkers.removeIf(w -> w.getGameObjId().equals(o.getId()));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean setGameObjectClass(Class<? extends GameObject> c) {
        if(gameObjClass != null) {
            return false;
        }
        gameObjClass = c;
        return true;
    }

    public void setTickRate(int milliseconds) {
        if(milliseconds < 1) {
            throw new RuntimeException("Tick rate can't be less than 1 millisecond!");
        }
        this.tick = milliseconds;
    }

    @Override
    public boolean isGameObjectClassSet() {
        return gameObjClass != null;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

}
