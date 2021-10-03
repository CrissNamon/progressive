package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

@IsGameScript
public class EchoSystem implements GameScript {

    private GameObject parent;

    public EchoSystem() {

    }

    private void start() {
        System.out.println("ECHO SYSTEM START");
        BasicGamePublisher.getInstance().subscribeOn("EchoInput", (input) -> System.out.println("ECHO OUTPUT: " + input));
    }

    private void update() {

    }

    @Override
    public GameObject gameObject() {
        return parent;
    }

    @Override
    public void setGameObject(GameObject parent) {
        this.parent = parent;
    }

    public void say(String message) {
        System.out.println(message);
    }
}
