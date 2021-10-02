package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.component.GameObject;

public interface Game {

    GameObject addGameObject();

    boolean setGameObjectClass(Class<? extends GameObject> c);

    boolean isGameObjectClassSet();
}
