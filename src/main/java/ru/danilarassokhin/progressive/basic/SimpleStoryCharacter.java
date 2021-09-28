package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryAction;
import ru.danilarassokhin.progressive.data.StoryCharacter;
import ru.danilarassokhin.progressive.data.StoryState;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleStoryCharacter implements StoryCharacter<Long, SimpleStoryInventory,
        SimpleStoryLocation, SimpleStoryItem,
        SimpleStoryQuest, String>, Serializable, AutoCloseable {

    private final Long id;
    private String name;
    private float health;
    private final SimpleStoryInventory inventory;
    private SimpleStoryLocation location;
    private transient final Set<SimpleStoryQuest> quests;
    private transient final Map<String, StoryAction> actions;

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

    protected SimpleStoryCharacter(Long id, String name, float health, SimpleStoryInventory inventory) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
        this.location = null;
        this.quests = new HashSet<>();
        actions = new HashMap<>();
        setHealth(health);
    }

    protected SimpleStoryCharacter(Long id) {
        this.id = id;
        this.name = "";
        this.inventory = new SimpleStoryInventory();
        this.location = null;
        this.quests = new HashSet<>();
        actions = new HashMap<>();
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

    /**
     * Sets character location
     * <br>
     * Be careful! If character current location is null then sets location regardless of entry condition
     * <br>
     * Changes StateManager state to LOCATION_MOVE_START(current location) and then
     * if current location is not null and has been successfully changed will change state to LOCATION_MOVE_COMPLETE(new location)
     * @param location new character location
     * @param onSuccess called if location successfully changes
     * @param onError called if location not changed
     * @return true if location has been changed
     */
    @Override
    public boolean setLocation(SimpleStoryLocation location, StoryAction onSuccess, StoryAction onError) {
        if(getLocation() == null) {
            this.location = location;
            return true;
        }
        SimpleStoryStateManager.getInstance()
                .setState(StoryState.LOCATION_MOVE_START, getLocation());
        if(location.canEntry()) {
            this.location = location;
            onSuccess.make();
            SimpleStoryStateManager.getInstance()
                    .setState(StoryState.LOCATION_MOVE_COMPLETE, getLocation());
        }else{
            onError.make();
        }
        return location.canEntry();
    }

    /**
     * Sets location with no success and error actions
     * @param location Location to set
     * @return true if location set successfully
     */
    public boolean setLocation(SimpleStoryLocation location) {
        return setLocation(location, () -> {}, () -> {});
    }

    @Override
    public boolean addItem(SimpleStoryItem item) {
        return getInventory().addItem(item);
    }

    @Override
    public StoryAction action(String actionName) {
        return actions.getOrDefault(actionName, () -> {});
    }

    @Override
    public boolean addAction(String actionName, StoryAction action) {
        return actions.putIfAbsent(actionName, action) == null;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }

    @Override
    public void close() throws ClassCastException {

    }
}
