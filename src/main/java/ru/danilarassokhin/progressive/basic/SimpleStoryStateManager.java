package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.data.StoryState;
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
    private transient Map<StoryState, List<StoryActionObject>> actions;

    private SimpleStoryStateManager() {
        actions = new HashMap<>();
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
        if(actions.containsKey(state)) {
            actions.get(state).forEach(a -> a.make(o));
        }else{
            actions.put(state, new ArrayList<>());
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
        return actions.getOrDefault(state, new ArrayList<>());
    }

    @Override
    @Deprecated
    public <V> void addAction(StoryState state, StoryActionObject<V> action) {
        addListener(state, action);
    }

    @Override
    public <V> void addListener(StoryState state, StoryActionObject<V> action) {
        actions.putIfAbsent(state, new ArrayList<>());
        actions.get(state).add(action);
    }
}
