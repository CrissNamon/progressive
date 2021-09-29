package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.component.StoryState;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.managers.StoryStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStoryStateManager implements StoryStateManager<StoryState>, Serializable {

    private static SimpleStoryStateManager INSTANCE;
    private StoryState state;
    private transient Map<StoryState, List<StoryActionObject>> listeners;

    private SimpleStoryStateManager() {
        listeners = new HashMap<>();
        setState(StoryState.UNDEFINED, null);
    }

    public static SimpleStoryStateManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStoryStateManager();
        }
        return INSTANCE;
    }

    @Override
    public StoryState getCurrentState() {
        return state;
    }

    @Override
    public <O> void setState(StoryState state, O o) {
        if(listeners.containsKey(state)) {
            listeners.get(state).forEach(a -> a.make(o));
        }else{
            listeners.put(state, new ArrayList<>());
        }
        this.state = state;
    }

    @Override
    @Deprecated
    public List<StoryActionObject> getActions(StoryState state) {
        return getListeners(state);
    }

    @Override
    public List<StoryActionObject> getListeners(StoryState state) {
        return listeners.getOrDefault(state, new ArrayList<>());
    }

    @Override
    @Deprecated
    public <V> void addAction(StoryState state, StoryActionObject<V> action) {
        addListener(state, action);
    }

    @Override
    public <V> void addListener(StoryState state, StoryActionObject<V> action) {
        listeners.putIfAbsent(state, new ArrayList<>());
        listeners.get(state).add(action);
    }
}
