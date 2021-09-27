package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.*;

import java.util.HashSet;
import java.util.Set;

public class SimpleStory implements Story<SimpleStoryNode, SimpleStoryCharacter,
        SimpleStoryLocation, SimpleStoryItem, SimpleStoryQuest> {

    private static SimpleStory INSTANCE;

    private final SimpleStoryGraph storyGraph;
    private final Set<SimpleStoryCharacter> storyCharacters;
    private final Set<SimpleStoryLocation> storyLocations;
    private final Set<SimpleStoryItem> storyItems;
    private final Set<SimpleStoryQuest> storyQuests;
    private SimpleStoryNode currentNode;
    private SimpleStoryStateManager stateManager;

    private SimpleStory() {
        this.storyGraph = new SimpleStoryGraph();
        this.storyCharacters = new HashSet<>();
        this.storyLocations = new HashSet<>();
        this.storyItems = new HashSet<>();
        this.storyQuests = new HashSet<>();
        this.currentNode = storyGraph.first();
        this.stateManager = SimpleStoryStateManager.getInstance();
    }

    public static SimpleStory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStory();
        }
        return INSTANCE;
    }

    @Override
    public SimpleStoryNode begin(SimpleStoryNode startNode) {
        stateManager.setState(StoryState.INIT);
        currentNode = startNode;
        return currentNode;
    }

    @Override
    public SimpleStoryNode getCurrentNode() {
        return currentNode;
    }

    @Override
    public SimpleStoryNode setNext(SimpleStoryNode node) {
        currentNode = node;
        return getCurrentNode();
    }

    @Override
    public SimpleStoryNode next() {
        stateManager.setState(StoryState.NODE_TRANSITION_START);
        getStoryCharacters()
                .forEach(character -> {
                    character.getQuests()
                            .forEach(SimpleStoryQuest::complete);
                    character.getQuests()
                            .removeIf(quest -> quest.isCompleted() && !quest.isUnique());
                });
        stateManager.setState(StoryState.NODE_TRANSITION_END);
        return currentNode;
    }

    @Override
    public boolean addStoryCharacter(SimpleStoryCharacter character) {
        return storyCharacters.add(character);
    }

    @Override
    public Set<SimpleStoryCharacter> getStoryCharacters() {
        return storyCharacters;
    }

    @Override
    public boolean addStoryLocation(SimpleStoryLocation location) {
        return storyLocations.add(location);
    }

    @Override
    public Set<SimpleStoryLocation> getStoryLocations() {
        return storyLocations;
    }

    @Override
    public Set<SimpleStoryItem> getStoryItems() {
        return storyItems;
    }

    @Override
    public boolean addStoryItem(SimpleStoryItem item) {
        return storyItems.add(item);
    }

    @Override
    public boolean addStoryQuest(SimpleStoryQuest quest) {
        return storyQuests.add(quest);
    }

    @Override
    public boolean isCharacterRegistered(SimpleStoryCharacter character) {
        return getStoryCharacters().contains(character);
    }

    @Override
    public boolean isLocationRegistered(SimpleStoryLocation location) {
        return getStoryLocations().contains(location);
    }

    @Override
    public boolean isItemRegistered(SimpleStoryItem item) {
        return getStoryItems().contains(item);
    }

    /**
     * Creates new character and adds it to Story
     * @param id
     * @param name
     * @param health
     * @param startLocation
     * @return
     */
    public SimpleStoryCharacter addStoryCharacter(Long id, String name, float health, SimpleStoryLocation startLocation) {
        SimpleStoryCharacter character = new SimpleStoryCharacter(id, name, health, new SimpleStoryInventory(), startLocation);
        addStoryCharacter(character);
        return character;
    }

    /**
     * Creates new character and adds it to Story
     * @param id
     * @param name
     * @param health
     * @param startLocation
     * @param items
     * @return
     */
    public SimpleStoryCharacter addStoryCharacter(Long id, String name, float health, SimpleStoryLocation startLocation, SimpleStoryItem... items) {
        SimpleStoryCharacter character = new SimpleStoryCharacter(id, name, health, new SimpleStoryInventory(items), startLocation);
        addStoryCharacter(character);
        return character;
    }

    /**
     * Creates new location and adds it to Story
     * @param id
     * @param name
     * @return
     */
    public SimpleStoryLocation addStoryLocation(Long id, String name) {
        SimpleStoryLocation location = new SimpleStoryLocation(id, name);
        addStoryLocation(location);
        return location;
    }

    /**
     * Creates new location and adds it to Story
     * @param id
     * @param name
     * @param restriction
     * @return
     */
    public SimpleStoryLocation addStoryLocation(Long id, String name, StoryCondition restriction) {
        SimpleStoryLocation location = new SimpleStoryLocation(id, name, restriction);
        addStoryLocation(location);
        return location;
    }

    /**
     * Creates new item and adds it to Story
     * @param id
     * @param name
     * @param startCount
     * @return
     */
    public SimpleStoryItem addStoryItem(Long id, String name, float startCount) {
        SimpleStoryItem item = new SimpleStoryItem(id, name, startCount);
        addStoryItem(item);
        return item;
    }

    public SimpleStoryQuest addStoryQuest(Long id, String name, StoryCondition completeCondition, StoryObjectExtraAction<SimpleStoryQuest> onComplete) {
        SimpleStoryQuest quest = new SimpleStoryQuest(id, name, completeCondition, onComplete);
        addStoryQuest(quest);
        return quest;
    }

    public SimpleStoryQuest addStoryQuest(Long id, String name, boolean isUnique, StoryCondition completeCondition,
                                          StoryObjectExtraAction<SimpleStoryQuest> onComplete) {
        SimpleStoryQuest quest = new SimpleStoryQuest(id, name, isUnique, completeCondition, onComplete);
        addStoryQuest(quest);
        return quest;
    }
}
