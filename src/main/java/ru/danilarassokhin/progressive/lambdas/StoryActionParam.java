package ru.danilarassokhin.progressive.lambdas;

/**
 * @param <T> Object to pass as param
 */
public interface StoryActionParam<T>{
    /**
     * Makes action
     * @param param Param to pass
     */
    void make(T param);
}
