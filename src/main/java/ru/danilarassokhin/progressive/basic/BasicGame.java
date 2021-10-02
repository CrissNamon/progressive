package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;

import java.util.HashMap;
import java.util.Map;

public final class BasicGame implements Game {

    private static BasicGame INSTANCE;

    private Map<Long, GameNode> gameNodes;
    private Map<Long, GameObject> gameObjects;
    private BasicGameStateManager stateManager;

    private BasicGame() {
        gameNodes = new HashMap<>();
        gameObjects = new HashMap<>();
        stateManager = new BasicGameStateManager();
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
        Long lastId = gameObjects.keySet().stream().max(Long::compareTo).orElse(0L);
        GameObject gameObject = new BasicGameObject(++lastId);
        gameObjects.putIfAbsent(lastId, gameObject);
        return gameObject;
    }


}
