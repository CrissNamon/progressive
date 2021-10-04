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
     * Adds bean information to container and create
     * new object every time {@link ru.danilarassokhin.progressive.injection.DIContainer#getBean(String, Class)} called
     */
    OBJECT
}
