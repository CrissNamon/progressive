package ru.danilarassokhin.progressive.injection;

/**
 * Defines bean lifecycle
 */
public enum GameBeanCreationPolicy {
    /**
     * Create bean only once and reuse it all the time
     */
    SINGLETON,
    /**
     * Create new bean on every call
     */
    OBJECT
}
