package ru.danilarassokhin.tengine.basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.danilarassokhin.tengine.Story;
import ru.danilarassokhin.tengine.StoryCondition;
import ru.danilarassokhin.tengine.StoryObjectExtraAction;
import ru.danilarassokhin.tengine.StoryState;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SimpleStory implements Story<SimpleStoryNode, SimpleStoryCharacter,
        SimpleStoryLocation, SimpleStoryItem, SimpleStoryQuest>, Serializable {

    private transient static SimpleStory INSTANCE;

    private final Set<SimpleStoryCharacter> storyCharacters;
    private transient final Set<SimpleStoryLocation> storyLocations;
    private transient final Set<SimpleStoryItem> storyItems;
    private transient final Set<SimpleStoryQuest> storyQuests;
    private transient final Set<SimpleStoryNode> storyNodes;
    private SimpleStoryNode currentNode;
    private SimpleStoryStateManager stateManager;

    private SimpleStory() {
        this.storyCharacters = new HashSet<>();
        this.storyLocations = new HashSet<>();
        this.storyItems = new HashSet<>();
        this.storyQuests = new HashSet<>();
        this.storyNodes = new HashSet<>();
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
    public Set<SimpleStoryNode> getStoryNodes() {
        return storyNodes;
    }

    @Override
    public boolean addStoryNode(SimpleStoryNode node) {
        return storyNodes.add(node);
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
    public Set<SimpleStoryQuest> getStoryQuests() {
        return storyQuests;
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

    public SimpleStoryNode addStoryNode(Long id, String content) {
        SimpleStoryNode node = new SimpleStoryNode(id, content);
        addStoryNode(node);
        return node;
    }

    public SimpleStoryCharacter getCharacterById(Long id) {
        return getStoryCharacters().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public SimpleStoryQuest getQuestById(Long id) {
        return getStoryQuests().stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
    }

    public SimpleStoryItem getItemById(Long id) {
        return getStoryItems().stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }
}
