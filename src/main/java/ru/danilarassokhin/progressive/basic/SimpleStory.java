package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Story;
import ru.danilarassokhin.progressive.data.StoryState;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleStory implements Story<Long, SimpleStoryNode, SimpleStoryCharacter,
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
    public SimpleStoryCharacter addStoryCharacter(Long id) {
        SimpleStoryCharacter character = new SimpleCharacterBuilder(1L).build();
        if(storyCharacters.putIfAbsent(id, character) == null) {
            return character;
        }else{
            return null;
        }
    }

    @Override
    public Map<Long, SimpleStoryNode> getStoryNodes() {
        return storyNodes;
    }

    @Override
    public SimpleStoryNode addStoryNode(Long id) {
        SimpleStoryNode node = new SimpleStoryNode(id);
        if(storyNodes.putIfAbsent(id, node) == null) {
            return node;
        }else{
            return null;
        }
    }

    @Override
    public Map<Long, SimpleStoryCharacter> getStoryCharacters() {
        return storyCharacters;
    }

    @Override
    public SimpleStoryLocation addStoryLocation(Long id) {
        SimpleStoryLocation location = new SimpleLocationBuilder(id).build();
        if(storyLocations.putIfAbsent(id, location) == null) {
            return location;
        }else{
            return null;
        }
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
    public SimpleStoryItem addStoryItem(Long id) {
        SimpleStoryItem item = new SimpleItemBuilder(id).build();
        if(storyItems.putIfAbsent(id, item) == null) {
            return item;
        }else{
            return null;
        }
    }

    @Override
    public SimpleStoryQuest addStoryQuest(Long id) {
        SimpleStoryQuest quest = new SimpleQuestBuilder(id).build();
        if(storyQuests.putIfAbsent(quest.getId(), quest) == null) {
            return quest;
        }else{
            return null;
        }
    }

    @Override
    public Map<Long, SimpleStoryQuest> getStoryQuests() {
        return storyQuests;
    }

    @Override
    public boolean isCharacterRegistered(Long id) {
        return getStoryCharacters().containsKey(id);
    }

    @Override
    public boolean isLocationRegistered(Long id) {
        return getStoryLocations().containsKey(id);
    }

    @Override
    public boolean isItemRegistered(Long id) {
        return getStoryItems().containsKey(id);
    }

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
