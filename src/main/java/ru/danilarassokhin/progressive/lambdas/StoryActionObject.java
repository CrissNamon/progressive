package ru.danilarassokhin.progressive.lambdas;

/**
 * Represents some story action
 * @param <O> Action parameter type
 */
public interface StoryActionObject<O>{
    /**
     * Makes action
     * @param obj Action parameter
     */
    void make(O obj);
}
