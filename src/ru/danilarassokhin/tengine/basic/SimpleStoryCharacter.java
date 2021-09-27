package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryExtraAction;
import ru.danilarassokhin.tengine.StoryCharacter;
import ru.danilarassokhin.tengine.StoryState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleStoryCharacter implements StoryCharacter<Long, SimpleStoryInventory,
        SimpleStoryLocation, SimpleStoryItem,
        SimpleStoryQuest, String> {

    private final Long id;
    private String name;
    private float health;
    private final SimpleStoryInventory inventory;
    private SimpleStoryLocation location;
    private final Set<SimpleStoryQuest> quests;
    private final Map<String, StoryExtraAction> actions;

    protected SimpleStoryCharacter(Long id, String name, float health, SimpleStoryInventory inventory,
                                SimpleStoryLocation location) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
        this.location = location;
        this.quests = new HashSet<>();
        actions = new HashMap<>();
        setHealth(health);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public void addHealth(float add) {
        this.health += add;
    }

    @Override
    public boolean addQuest(SimpleStoryQuest quest) {
        return quests.add(quest);
    }

    @Override
    public Set<SimpleStoryQuest> getQuests() {
        return quests;
    }

    @Override
    public SimpleStoryInventory getInventory() {
        return inventory;
    }

    @Override
    public SimpleStoryLocation getLocation() {
        return location;
    }

    @Override
    public boolean setLocation(SimpleStoryLocation location, StoryExtraAction onSuccess, StoryExtraAction onError) {
        SimpleStoryStateManager.getInstance()
                .setState(StoryState.LOCATION_MOVE_START);
        if(location.canEntry()) {
            this.location = location;
            onSuccess.make();
            SimpleStoryStateManager.getInstance()
                    .setState(StoryState.LOCATION_MOVE_COMPLETE);
        }else{
            onError.make();
        }
        return location.canEntry();
    }

    @Override
    public boolean addItem(SimpleStoryItem item) {
        return getInventory().addItem(item);
    }

    @Override
    public StoryExtraAction action(String actionName) {
        return actions.getOrDefault(actionName, () -> {});
    }

    @Override
    public boolean addAction(String actionName, StoryExtraAction action) {
        return actions.putIfAbsent(actionName, action) == null;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
