package ru.danilarassokhin.progressive;

/**
 * @param <T> Object to pass as param
 */
public interface StoryExtraActionParam<T>{
    /**
     * Makes action
     * @param param Param to pass
     */
    void make(T param);
}
