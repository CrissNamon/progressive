package ru.danilarassokhin.tengine;

/**
 * Represents some story action
 * @param <O> Action parameter type
 */
public interface StoryObjectExtraAction<O>{
    /**
     * Makes action
     * @param obj Action parameter
     */
    void make(O obj);
}
