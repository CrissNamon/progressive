package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.StoryExtraActionParam;
import ru.danilarassokhin.progressive.StoryState;
import ru.danilarassokhin.progressive.StoryStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStoryStateManager implements StoryStateManager<StoryState>, Serializable {

    private static SimpleStoryStateManager INSTANCE;
    private StoryState state;
    private transient Map<StoryState, List<StoryExtraActionParam>> actions;

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
    public List<StoryExtraActionParam> getActions(StoryState state) {
        return actions.getOrDefault(state, new ArrayList<>());
    }

    @Override
    public <V> void addAction(StoryState state, StoryExtraActionParam<V> action) {
        actions.putIfAbsent(state, new ArrayList<>());
        actions.get(state).add(action);
    }
}
