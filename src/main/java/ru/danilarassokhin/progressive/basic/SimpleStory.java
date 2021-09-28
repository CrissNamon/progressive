package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Story;
import ru.danilarassokhin.progressive.data.StoryState;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleStory implements Story<SimpleStoryNode, SimpleStoryCharacter,
        SimpleStoryLocation, SimpleStoryItem, SimpleStoryQuest>, Serializable {

    private transient static SimpleStory INSTANCE;

    private final Map<Long, SimpleStoryCharacter> storyCharacters;
    private transient final Map<Long, SimpleStoryLocation> storyLocations;
    private transient final Map<Long, SimpleStoryItem> storyItems;
    private transient final Map<Long, SimpleStoryQuest> storyQuests;
    private transient final Map<Long, SimpleStoryNode> storyNodes;
    private SimpleStoryNode currentNode;
    private SimpleStoryStateManager stateManager;

    private SimpleStory() {
        this.storyCharacters = new HashMap<>();
        this.storyLocations = new HashMap<>();
        this.storyItems = new HashMap<>();
        this.storyQuests = new HashMap<>();
        this.storyNodes = new HashMap<>();
        this.stateManager = SimpleStoryStateManager.getInstance();
        stateManager.setState(StoryState.INIT, this);
    }

    public static SimpleStory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStory();
        }
        return INSTANCE;
    }

    @Override
    public SimpleStoryNode begin(SimpleStoryNode startNode) {
        currentNode = startNode;
        return currentNode;
    }

    @Override
    public SimpleStoryNode getCurrentNode() {
        return currentNode;
    }

    /**
     * Sets next node
     * <br>
     * Changes StateManager state to NODE_TRANSITION_START(current node) then sets new node
     * @param node Next node
     * @return {@code node}
     */
    @Override
    public SimpleStoryNode setNext(SimpleStoryNode node) {
        stateManager.setState(StoryState.NODE_TRANSITION_START, getCurrentNode());
        currentNode = node;
        return getCurrentNode();
    }

    /**
     * Go to next node
     * <br>
     * Changes StateManager state to NODE_TRANSITION_END(new node)
     * @return next node
     */
    @Override
    public SimpleStoryNode next() {
        getStoryCharacters().values()
                .forEach(character -> {
                    character.getQuests()
                            .forEach(SimpleStoryQuest::complete);
                    character.getQuests()
                            .removeIf(quest -> quest.isCompleted() && !quest.isUnique());
                });
        stateManager.setState(StoryState.NODE_TRANSITION_END, getCurrentNode());
        return currentNode;
    }

    @Override
    public boolean addStoryCharacter(SimpleStoryCharacter character) {
        return storyCharacters.putIfAbsent(character.getId(), character) == null;
    }

    @Override
    public Map<Long, SimpleStoryNode> getStoryNodes() {
        return storyNodes;
    }

    @Override
    public boolean addStoryNode(SimpleStoryNode node) {
        return storyNodes.putIfAbsent(node.getId(), node) == null;
    }

    @Override
    public Map<Long, SimpleStoryCharacter> getStoryCharacters() {
        return storyCharacters;
    }

    @Override
    public boolean addStoryLocation(SimpleStoryLocation location) {
        return storyLocations.putIfAbsent(location.getId(), location) == null;
    }

    @Override
    public Map<Long, SimpleStoryLocation> getStoryLocations() {
        return storyLocations;
    }

    @Override
    public Map<Long, SimpleStoryItem> getStoryItems() {
        return storyItems;
    }

    @Override
    public boolean addStoryItem(SimpleStoryItem item) {
        return storyItems.putIfAbsent(item.getId(), item) == null;
    }

    @Override
    public boolean addStoryQuest(SimpleStoryQuest quest) {
        return storyQuests.putIfAbsent(quest.getId(), quest) == null;
    }

    @Override
    public Map<Long, SimpleStoryQuest> getStoryQuests() {
        return storyQuests;
    }

    @Override
    public boolean isCharacterRegistered(SimpleStoryCharacter character) {
        return getStoryCharacters().containsKey(character.getId());
    }

    @Override
    public boolean isLocationRegistered(SimpleStoryLocation location) {
        return getStoryLocations().containsKey(location.getId());
    }

    @Override
    public boolean isItemRegistered(SimpleStoryItem item) {
        return getStoryItems().containsKey(item.getId());
    }

    @Override
    public SimpleStoryCharacter getCharacterById(Long id) {
        return getStoryCharacters().getOrDefault(id, null);
    }

    @Override
    public SimpleStoryQuest getQuestById(Long id) {
        return getStoryQuests().getOrDefault(id, null);
    }

    @Override
    public SimpleStoryItem getItemById(Long id) {
        return getStoryItems().getOrDefault(id, null);
    }

    @Override
    public SimpleStoryLocation getLocationById(Long id) {
        return getStoryLocations().getOrDefault(id, null);
    }

}
