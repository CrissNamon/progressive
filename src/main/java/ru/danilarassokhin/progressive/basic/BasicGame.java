package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.util.HashMap;
import java.util.Map;

public final class BasicGame implements Game {

    private static BasicGame INSTANCE;
    private final Map<Long, GameObject> gameObjects;
    private BasicGameStateManager stateManager;
    private Class<? extends GameObject> gameObjClass;

    private BasicGame() {
        gameObjects = new HashMap<>();
        stateManager = BasicGameStateManager.getInstance();
        stateManager.setState(GameState.INIT, this);
    }

    public static BasicGame getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGame();
        }
        return INSTANCE;
    }

    @Override
    public GameObject addGameObject() {
        if(!isGameObjectClassSet()) {
            throw new RuntimeException("GameObject class has not been set in game! Use setGameObjectClass method in your game");
        }
        Long lastId = gameObjects.keySet().stream().max(Long::compareTo).orElse(0L);
        GameObject gameObject = ComponentCreator.create(gameObjClass, ++lastId);
        gameObjects.putIfAbsent(lastId, gameObject);
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

    @Override
    public boolean isGameObjectClassSet() {
        return gameObjClass != null;
    }

}
