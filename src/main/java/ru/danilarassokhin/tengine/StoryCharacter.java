package ru.danilarassokhin.tengine;

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
public interface StoryCharacter<I, N extends StoryInventory,
        L extends StoryLocation, T extends StoryItem,
        Q extends StoryQuest, AN> {

    /**
     * @return character id
     */
    I getId();

    /**
     * @return character name
     */
    String getName();

    /**
     * Sets character name
     * @param name new name
     */
    void setName(String name);

    /**
     * @return character's current health
     */
    float getHealth();

    /**
     * Sets character health
     * @param health new health value
     */
    void setHealth(float health);

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
     * @return character's quests
     */
    Set<Q> getQuests();

    /**
     * @return character inventory
     */
    N getInventory();

    /**
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
    boolean setLocation(L location, StoryExtraAction onSuccess, StoryExtraAction onError);

    /**
     * Adds item to character
     * @param item new item
     * @return true if items has been added
     */
    boolean addItem(T item);

    /**
     * Gets character action by id
     * @param actionName Action name to get
     * @return Action or null
     */
    StoryExtraAction action(AN actionName);

    /**
     * Adds action to character
     * @param actionName Action name to add
     * @param action Action to add
     * @return true if action doesn't exists already
     */
    boolean addAction(AN actionName, StoryExtraAction action);

}
