package ru.danilarassokhin.progressive.data;

/**
 * Represents story location
 * @param <I> Location id type
 */
public interface StoryLocation<I> {

    /**
     * @return Location id
     */
    I getId();

    /**
     * @return Location name
     */
    String getName();

    /**
     * Sets location name
     * @param name New location name
     */
    void setName(String name);

    /**
     * @return true if entry condition is completed
     */
    boolean canEntry();

}
