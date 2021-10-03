package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;

@IsGameScript
public class LocationSystem extends AbstractGameScript{

    private LocationSystem(BasicGameObject parent) {
        super(parent);
    }

    private void start() {
        System.out.println("LOCATION SYSTEM START");
        BasicGamePublisher.getInstance().subscribeOn("EchoInput", (input) -> System.out.println("LOCATION OUTPUT: " + input));
    }

    private void update() {

    }

}
