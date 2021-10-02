package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

public abstract class AbstractGameScript implements GameScript {

    protected final GameObject parent;

    protected AbstractGameScript(GameObject parent) {
        this.parent = parent;
    }

    @Override
    public final GameObject gameObject() {
        return parent;
    }
}
