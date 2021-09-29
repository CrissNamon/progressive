package ru.danilarassokhin.progressive.data;

import ru.danilarassokhin.progressive.lambdas.StoryAction;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;

import java.util.Set;

/**
 * Represents story character
 * @param <I> Character ID type
 * @param <N> Character inventory type
 * @param <L> Character location type
 * @param <T> Character items type
 * @param <Q> Character quests type
 * @param <AN> Character action id type
 */
public interface StoryCharacter<I, C, N extends StoryInventory,
        L extends StoryLocation, T extends StoryItem,
        Q extends StoryQuest, AN> extends StoryComponent<I> {

    /**
     * Returns character id
     * @return character id
     */
    I getId();

    /**
     * Returns character name
     * @return character name
     */
    String getName();

    /**
     * Sets character name
     * @param name new name
     * @return This character
     */
    C setName(String name);

    /**
     * Returns character's health
     * @return character's current health
     */
    float getHealth();

    /**
     * Sets character health
     * @param health new health value
     * @return This character
     */
    C setHealth(float health);

    /**
     * Adds health to character
     * @param add health to add
     */
    void addHealth(float add);

    /**
     * Adds quest to character
     * @param quest quest to add
     * @return true if quest doesn't exists already
     */
    boolean addQuest(Q quest);

    /**
     * Returns all quests assigned to this character
     * @return character's quests
     */
    Set<Q> getQuests();

    /**
     * Returns inventory of this character
     * @return character inventory
     */
    N getInventory();

    /**
     * Returns location of this character
     * @return character location
     */
    L getLocation();

    /**
     * Sets character location
     * @param location new character location
     * @param onSuccess called if location successfully changes
     * @param onError called if location not changed
     * @return true if location has been changed
     */
    boolean setLocation(L location, StoryAction onSuccess, StoryAction onError);

    /**
     * Adds item to character
     * @param item new item
     * @return true if items has been added
     */
    boolean addItem(T item);

    /**
     * Does character action with {@code actionName}
     * @param actionName Action name to get
     * @param <O> Action param type to pass
     * @param param Action parameter
     * @return true if action is done
     */
    <O> boolean action(AN actionName, O param);

    /**
     * Does character action with {@code actionName} without parameters
     * @param actionName Action name to get
     * @return Action or null
     */
    boolean action(AN actionName);

    /**
     * Adds action to character
     * @param actionName Action name to add
     * @param action Action to add
     * @param <O> Action param type to pass
     * @return true if action doesn't exists already
     */
    <O> boolean addAction(AN actionName, StoryActionObject<O> action);

}
