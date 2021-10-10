package ru.danilarassokhin.progressive.basic.script;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

//Simple Echo script
@IsGameScript //This annotation is required for game scripts
public class EchoSystem implements GameScript {

    //Parent GameObject this script will be attached to
    private GameObject parent;

    //Subscribe to game global update with GamePublisher and pass update delta time to say method
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

    //This will be called on global game update
    private void say(Object message) {
        System.out.println(message);
    }
}
