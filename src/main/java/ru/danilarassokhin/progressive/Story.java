package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.basic.*;
import ru.danilarassokhin.progressive.data.*;

import java.util.Map;


/**
 * Represents game story
 * <br>Story must be Singleton!
 * @param <N> Story nodes type {@link StoryNode}
 * @param <C> Story characters type {@link StoryCharacter}
 * @param <L> Story locations type {@link StoryLocation}
 */
public interface Story<N extends StoryNode, C extends StoryCharacter,
        L extends StoryLocation, I extends StoryItem,
        Q extends StoryQuest> {

    /**
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
     * @param character Character to add
     */
    boolean addStoryCharacter(C character);

    /**
     * @return Map of registered story nodes as nodeId -> node
     */
    Map<?, N> getStoryNodes();

    /**
     * @param node Node to add
     * @return true if node doesn't exists already
     */
    boolean addStoryNode(N node);

    /**
     * @return All characters registered in story
     */
    Map<?, C> getStoryCharacters();

    /**
     * Adds location to story
     * @param location Location to add
     */
    boolean addStoryLocation(L location);

    /**
     * @return Map of locations registered in story as locationId -> location
     */
    Map<?, L> getStoryLocations();

    /**
     * @return All items registered in story as itemId -> item
     */
    Map<?, I> getStoryItems();

    /**
     * Adds item to story
     * @param item Item to add
     * @return true if item doesn't exists already
     */
    boolean addStoryItem(I item);

    /**
     * Adds quest to story
     * @param quest Quest to add
     * @return true if quest doesn't exists already
     */
    boolean addStoryQuest(Q quest);

    /**
     * @return All quests registered in story as questId -> quest
     */
    Map<?, Q> getStoryQuests();

    /**
     * Sets current node in Story to {@code startNode}
     * @param startNode Node to set
     * @return {@code startNode}
     */
    SimpleStoryNode begin(N startNode);

    /**
     * Checks if {@code character} registered in Story
     * @param character Character to check
     * @return true if {@code character} is registered
     */
    boolean isCharacterRegistered(C character);

    /**
     * Checks if {@code location} registered in Story
     * @param location Location to check
     * @return true if {@code location} is registered
     */
    boolean isLocationRegistered(L location);

    /**
     * Checks if {@code item} registered in Story
     * @param item Item to check
     * @return true if {@code item} is registered
     */
    boolean isItemRegistered(I item);

    /**
     * Searches for character registered in story by id
     * @param id Id to search
     * @return Character, null otherwise
     */
    C getCharacterById(Long id);

    /**
     * Searches for quest registered in story by id
     * @param id Id to search
     * @return Quest, null otherwise
     */
    Q getQuestById(Long id);

    /**
     * Searches for item registered in story by id
     * @param id Id to search
     * @return Item, null otherwise
     */
    I getItemById(Long id);

    /**
     * Searches for location registered in story by id
     * @param id Id to search
     * @return Location, null otherwise
     */
    L getLocationById(Long id);

}
