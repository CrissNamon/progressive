package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.basic.system.AbstractGameScript;
import ru.danilarassokhin.progressive.component.GameObject;

public class MyGameObject implements GameObject {

    private Long id;

    public MyGameObject(Long id) {
        this.id = id;
    }

    @Override
    public <V extends AbstractGameScript> V getGameScript(Class<V> gameScriptClass) {
        return null;
    }

    @Override
    public <V extends AbstractGameScript> boolean hasGameScript(Class<V> gameScriptClass) {
        return false;
    }

    @Override
    public Long getId() {
        return null;
    }

    private void start() {

    }

    private void update() {

    }
}
