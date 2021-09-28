package ru.danilarassokhin.tengine;

import ru.danilarassokhin.tengine.basic.SimpleStoryNode;

import java.util.Set;


/**
 * Represents game story
 * <br>Story must be Singleton!
 * @param <N> Story nodes type {@link ru.danilarassokhin.tengine.StoryNode}
 * @param <C> Story characters type {@link ru.danilarassokhin.tengine.StoryCharacter}
 * @param <L> Story locations type {@link ru.danilarassokhin.tengine.StoryLocation}
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
     * <br>
     * Changes StateManager state as <CURRENT_STATE> -> NODE_TRANSITION_START -> NODE_TRANSITION_END
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
     * @return All characters registered in story
     */
    Set<C> getStoryCharacters();

    /**
     * Adds location to story
     * @param location Location to add
     */
    boolean addStoryLocation(L location);

    /**
     * @return All locations registered in story
     */
    Set<L> getStoryLocations();

    /**
     * @return All items registered in story
     */
    Set<I> getStoryItems();

    /**
     * Adds item to story
     * @param item Item to add
     * @return true if item doesn't exists already
     */
    boolean addStoryItem(I item);

    boolean addStoryQuest(Q quest);

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

}
