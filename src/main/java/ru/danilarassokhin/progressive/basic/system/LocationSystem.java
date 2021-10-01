package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.annotation.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.annotation.isGameScript;

@isGameScript
@GameBean(policy = GameBeanCreationPolicy.OBJECT)
public class LocationSystem extends AbstractGameScript{
}
