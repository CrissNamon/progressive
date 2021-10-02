package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.main.Test;
import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

@ComponentScan(value = {"ru.danilarassokhin.progressive.basic.system"})
public class BasicConfiguration extends AbstractConfiguration {

    @GameBean(policy = GameBeanCreationPolicy.SINGLETON)
    public BasicObjectCaster objectCaster() {
        return new BasicObjectCaster();
    }

    @GameBean(qualifiers = {"objectCaster"})
    public Test create(BasicObjectCaster objectCaster) {
        return new Test();
    }

    @GameBean(qualifiers = {"create", "objectCaster"})
    public GameItem item(Test create, BasicObjectCaster objectCaster) {
        return new GameItem(1L);
    }

}
