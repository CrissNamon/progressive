package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.progressive.annotation.Components;
import ru.danilarassokhin.progressive.basic.component.GlobalIdGenerator;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

@Components({GlobalIdGenerator.class, GameItem.class})
public class TestConfiguration extends AbstractConfiguration {


}
