package ru.danilarassokhin.progressive;

import java.util.Set;

/**
 * Represents character's inventory
 * @param <I> Inventory item type
 */
public interface StoryInventory<I extends StoryItem> {

    /**
     * Adds item to inventory
     * @param item Item to add
     * @return true if item doesn't exists
     */
    boolean addItem(I item);

    /**
     * Adds item to inventory
     * @param item Item to add
     * @param add Item initial count
     */
    void addItem(I item, float add);

    /**
     * Checks if item exists in inventory
     * @param item Item to search
     * @return true if item exists in inventory
     */
    boolean hasItem(I item);

    /**
     * Removes item from inventory
     * @param item Item to remove
     */
    void removeItem(I item);

    /**
     * @return Items in inventory
     */
    Set<I> getItems();
}
