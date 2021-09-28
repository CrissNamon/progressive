package ru.danilarassokhin.progressive.data;

/**
 * Represents story location
 * @param <I> Location id type
 */
public interface StoryLocation<I> {

    /**
     * Returns location id
     * @return Location id
     */
    I getId();

    /**
     * Returns location id
     * @return Location name
     */
    String getName();

    /**
     * Sets location name
     * @param name New location name
     */
    void setName(String name);

    /**
     * Checks if entry condition is done
     * @return true if entry condition is completed
     */
    boolean canEntry();

}
