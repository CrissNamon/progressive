package ru.danilarassokhin.progressive.basic.manager;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.annotation.Protected;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.manager.GameStateManager;
import ru.danilarassokhin.progressive.util.GameSecurityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicGameStateManager implements GameStateManager<GameState> {
    private static BasicGameStateManager INSTANCE;
    private GameState state;
    private transient Map<GameState, List<GameActionObject>> listeners;

    private BasicGameStateManager() {
        listeners = new HashMap<>();
        setState(GameState.UNDEFINED, null);
    }

    public static BasicGameStateManager getInstance() {
        if(INSTANCE == null) {
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
    public <O> void setState(GameState state, O o) {
        GameSecurityManager.allowAccessTo("This method can be called only from Game class. " +
                "Access denied", BasicGame.class, BasicGameStateManager.class);
        if(listeners.containsKey(state)) {
            listeners.getOrDefault(state, new ArrayList<>()).parallelStream().forEach(a -> a.make(o));
        }else{
            listeners.put(state, new ArrayList<>());
        }
        this.state = state;
    }

    @Override
    public List<GameActionObject> getListeners(GameState state) {
        return listeners.getOrDefault(state, new ArrayList<>());
    }

    @Override
    public <V> void addListener(GameState state, GameActionObject<V> action) {
        listeners.putIfAbsent(state, new ArrayList<>());
        listeners.get(state).add(action);
    }

}
