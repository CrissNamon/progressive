package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryExtraAction;
import ru.danilarassokhin.tengine.StoryState;
import ru.danilarassokhin.tengine.StoryStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStoryStateManager implements StoryStateManager<StoryState, StoryExtraAction>, Serializable {

    private static SimpleStoryStateManager INSTANCE;
    private StoryState state;
    private transient Map<StoryState, List<StoryExtraAction>> actions;

    private SimpleStoryStateManager() {
        actions = new HashMap<>();
        setState(StoryState.INIT);
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
    public void setState(StoryState state) {
        if(actions.containsKey(state)) {
            actions.get(state).forEach(StoryExtraAction::make);
        }else{
            actions.put(state, new ArrayList<>());
        }
        this.state = state;
    }

    @Override
    public List<StoryExtraAction> getActions(StoryState state) {
        return actions.getOrDefault(state, new ArrayList<>());
    }

    @Override
    public void addAction(StoryState state, StoryExtraAction action) {
        actions.putIfAbsent(state, new ArrayList<>());
        actions.get(state).add(action);
    }
}
