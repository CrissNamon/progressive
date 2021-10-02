package ru.danilarassokhin.progressive.basic.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.manager.GameStateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicGameStateManager implements GameStateManager<GameState> {
    private static BasicGameStateManager INSTANCE;
    private GameState state;
    private transient Map<GameState, List<GameActionObject>> listeners;

    public BasicGameStateManager() {
        listeners = new HashMap<>();
        setState(GameState.UNDEFINED, null);
    }

    @Override
    public GameState getCurrentState() {
        return state;
    }

    @Override
    public <O> void setState(GameState state, O o) {
        if(listeners.containsKey(state)) {
            listeners.get(state).forEach(a -> a.make(o));
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
