package ru.danilarassokhin.tengine;

/**
 * Represents story item
 * @param <I> Item id type
 */
public interface StoryItem<I>{

    /**
     * @return Item id
     */
    I getId();

    /**
     * @return Item name
     */
    String getName();

    /**
     * Sets item name
     * @param name Item name
     */
    void setName(String name);

    /**
     * @return Item count
     */
    float getCount();

    /**
     * Sets item count
     * @param count new count
     */
    void setCount(float count);

    /**
     * Adds item's count
     * @param add Count to add
     */
    void add(float add);
}
