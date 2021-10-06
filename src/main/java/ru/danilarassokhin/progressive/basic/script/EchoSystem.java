package ru.danilarassokhin.progressive.basic.script;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

@IsGameScript
public class EchoSystem implements GameScript {

    private GameObject parent;
    private int id;

    public EchoSystem() {
        BasicGamePublisher.getInstance().subscribeOn("update", this::say);
    }

    @Override
    public GameObject gameObject() {
        return parent;
    }

    @Override
    public void setGameObject(GameObject parent) {
        this.parent = parent;
    }

    private void say(Object message) {
        System.out.println(message);
    }
}
