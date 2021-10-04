package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.basic.component.GlobalIdGenerator;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.util.ComponentCreator;

public class TestConfiguration extends AbstractConfiguration {

    @GameBean(name = "globalIdGenerator", order = 1)
    public GlobalIdGenerator idGenerator() {
        return new GlobalIdGenerator();
    }

    @GameBean(name = "generator", policy = GameBeanCreationPolicy.OBJECT, order = 2)
    public Long generator() {
        return BasicDIContainer.getInstance().getBean("globalIdGenerator", GlobalIdGenerator.class).next();
    }

    @GameBean(name = "test", qualifiers = "generator", order = 3)
    public Long test(GlobalIdGenerator generator) {
        return generator.next();
    }

    @GameBean(name = "gameItem", order = 4)
    public GameItem getItem() {
        return ComponentCreator.create(GameItem.class);
    }

}
