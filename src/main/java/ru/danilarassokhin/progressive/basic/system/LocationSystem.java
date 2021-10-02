package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.component.GameObject;

@IsGameScript
public class LocationSystem extends AbstractGameScript{

    public LocationSystem(BasicGameObject parent) {
        super(parent);
    }
}
