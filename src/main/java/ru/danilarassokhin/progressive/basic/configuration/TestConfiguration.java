package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

public class TestConfiguration extends AbstractConfiguration {

    @GameBean
    public GameItem createItem() {
        return new GameItem(1L);
    }

}
