package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

public abstract class AbstractGameScript implements GameScript {

    private GameObject parent;

    @Override
    public GameObject getParent() {
        return parent;
    }

    @Override
    public <O extends GameObject> void setParent(O object) {
        this.parent = object;
    }

}
