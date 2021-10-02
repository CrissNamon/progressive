package ru.danilarassokhin.progressive.injection;

import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

/**
 * Represents Dependency Injection container
 */
public interface DIContainer {

    /**
     * Loads configuration from class
     * @param config Configuration class
     * @param <C> Configuration class type
     */
    <C extends AbstractConfiguration> void loadConfiguration(Class<C> config);

    /**
     * Gets bean by it's name and class
     * @param name Bean name to find
     * @param beanClass Bean class to find
     * @param <V> Bean object to return
     * @return Bean object or throws RuntimeException if bean not found
     */
    <V> V getBean(String name, Class<V> beanClass);

    /**
     * Gets bean by it's class
     * @param beanClass Bean class to find
     * @param <V> Bean object to return
     * @return Bean object or throws RuntimeException if bean not found
     */
    <V> V getBean(Class<V> beanClass);

}
