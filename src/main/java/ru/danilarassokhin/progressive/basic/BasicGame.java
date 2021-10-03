package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.ComponentCreator;
import ru.danilarassokhin.progressive.util.GameSecurityManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

    public void start() throws Throwable {
        BasicObjectCaster basicObjectCaster = new BasicObjectCaster();
        GameSecurityManager.denyAccessIf("Game has been already started!", () -> isStarted);
        isStarted = true;
        gameObjectWorkers.parallelStream().forEach(this::callStartInGameObject);
        if(!isStatic) {
            update();
        }
    }

    private void callStartInGameObject(GameObjectWorker worker) {
        try {
            worker.getStartMethod()
                    .invoke(
                            gameObjects.get(worker.getGameObjId())
                    );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("Error occurred during calling start method on GameObject " + worker.getGameObjId());
        }
    }

    private void callUpdateInGameObject(GameObjectWorker worker) {
        scheduler
                .scheduleAtFixedRate(() -> {
                    try {
                        worker.getUpdateMethod().invoke(
                                gameObjects.get(
                                        worker.getGameObjId()
                                )
                        );
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }, 0, tick, TimeUnit.MILLISECONDS);
    }

    public void update() {
        GameSecurityManager.allowAccessIf("Game param isStatic is set to false. Can't update manually!",
                () -> isStatic && isStarted
        || GameSecurityManager.getCallerClass().equals(BasicGame.class));
        gameObjectWorkers.parallelStream().forEach(this::callUpdateInGameObject);
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
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Method update = gameObjClass.getDeclaredMethod("update");
            Method start = gameObjClass.getDeclaredMethod("start");
            if(!ComponentCreator.isModifierSet(update.getModifiers(), Modifier.PRIVATE)
                || !ComponentCreator.isModifierSet(start.getModifiers(), Modifier.PRIVATE)) {
                throw new RuntimeException("GameObject must have methods private void update() and private void start() in " + gameObjClass.getName());
            }
            update.setAccessible(true);
            start.setAccessible(true);
            gameObjectWorkers.add(new GameObjectWorker(lookup.unreflect(update), lookup.unreflect(start), lastId));
            return gameObject;
        }catch (NoSuchMethodException | IllegalAccessException e) {
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
