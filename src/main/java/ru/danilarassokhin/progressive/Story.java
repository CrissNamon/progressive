package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.data.*;

import java.util.Map;


/**
 * Represents game story
 * <br>Story must be Singleton!
 * @param <N> Story nodes type {@link StoryNode}
 * @param <C> Story characters type {@link StoryCharacter}
 * @param <L> Story locations type {@link StoryLocation}
 * @param <I> Story item type {@link StoryItem}
 * @param <Q> Story quest type {@link StoryQuest}
 */
public interface Story<ID, N extends StoryNode, C extends StoryCharacter,
        L extends StoryLocation, I extends StoryItem,
        Q extends StoryQuest> {

    /**
     * Returns current node
     * @return current story node
     */
    N getCurrentNode();

    /**
     * Go to next node
     * @return next node
     */
    N next();

    /**
     * Sets next node
     * @param node Next node
     * @return {@code node}
     */
    N setNext(N node);

    /**
     * Adds character to story
     * @param id Character id to add
     * @return Empty character or null if id already exists
     */
    C addStoryCharacter(ID id);

    /**
     * Returns all nodes registered in story
     * @return Map of nodes as nodeId - node
     */
    Map<ID, N> getStoryNodes();

    /**
     * Adds node to story
     * @param id Node id to add
     * @return Empty node or null if id already exists
     */
    N addStoryNode(ID id);

    /**
     * Returns all characters registered in story
     * @return Map of characters as characterId - character
     */
    Map<ID, C> getStoryCharacters();

    /**
     * Adds location to story
     * @param id Location id to add
     * @return Empty location or null if id already exists
     */
    L addStoryLocation(ID id);

    /**
     * Returns all location registered in story
     * @return Map of locations as characterId - character
     */
    Map<ID, L> getStoryLocations();

    /**
     * Returns all items registered in story
     * @return Map of items as characterId - character
     */
    Map<ID, I> getStoryItems();

    /**
     * Adds item to story
     * @param id Item id to add
     * @return Empty item or null if id already exists
     */
    I addStoryItem(ID id);

    /**
     * Adds quest to story
     * @param id Quest id to add
     * @return Empty item or null if id already exists
     */
    Q addStoryQuest(ID id);

    /**
     * Returns all quests registered in story
     * @return Map of quests as characterId - character
     */
    Map<ID, Q> getStoryQuests();

    /**
     * Sets current node in Story to {@code startNode}
     * @param startNode Node to set
     * @return {@code startNode}
     */
    N begin(N startNode);

    /**
     * Checks if {@code character} registered in Story
     * @param id Character id to check
     * @return true if {@code character} is registered
     */
    boolean isCharacterRegistered(ID id);

    /**
     * Checks if {@code location} registered in Story
     * @param id Location id to check
     * @return true if {@code location} is registered
     */
    boolean isLocationRegistered(ID id);

    /**
     * Checks if {@code item} registered in Story
     * @param id Item id to check
     * @return true if {@code item} is registered
     */
    boolean isItemRegistered(ID id);

    /**
     * Searches for character registered in story by id
     * @param id Id to search
     * @return Character, null otherwise
     */
    C getCharacterById(ID id);

    /**
     * Searches for quest registered in story by id
     * @param id Id to search
     * @return Quest, null otherwise
     */
    Q getQuestById(ID id);

    /**
     * Searches for item registered in story by id
     * @param id Id to search
     * @return Item, null otherwise
     */
    I getItemById(ID id);

    /**
     * Searches for location registered in story by id
     * @param id Id to search
     * @return Location, null otherwise
     */
    L getLocationById(ID id);

}
