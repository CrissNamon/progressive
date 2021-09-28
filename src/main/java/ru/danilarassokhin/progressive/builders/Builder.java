package ru.danilarassokhin.progressive.builders;

/**
 * Represents abstract builder
 * @param <V> Object type for building
 */
public interface Builder<V> {

    /**
     * Builds object
     * @return Built object
     */
    V build();

}
