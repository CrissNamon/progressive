package ru.danilarassokhin.progressive;

public interface StorySaveManager<S extends Story, T> {

    T save();

    S load(T save);

}
