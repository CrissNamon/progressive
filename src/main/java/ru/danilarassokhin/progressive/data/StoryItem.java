package ru.danilarassokhin.progressive.data;

/**
 * Represents story item
 * @param <I> Item id type
 */
public interface StoryItem<I> extends StoryComponent<I>{

    /**
     * Returns item id
     * @return Item id
     */
    I getId();

    /**
     * Return item name
     * @return Item name
     */
    String getName();

    /**
     * Sets item name
     * @param name Item name
     */
    void setName(String name);

    /**
     * Returns item amount
     * @return Item amount
     */
    float getAmount();

    /**
     * Sets item count
     * @param amount new amount
     */
    void setAmount(float amount);

    /**
     * Adds item's count
     * @param add Count to add
     */
    void add(float add);
}
