package ru.danilarassokhin.progressive.basic.abstraction;

import ru.danilarassokhin.progressive.component.*;
import ru.danilarassokhin.progressive.lambdas.StoryAction;

import java.util.Set;

public interface AllSystemsCharacter<I, C, N extends StoryInventory,
        L extends StoryLocation, T extends StoryItem,
        Q extends StoryQuest, AN> extends StoryCharacter<I, C, AN> {

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
}
