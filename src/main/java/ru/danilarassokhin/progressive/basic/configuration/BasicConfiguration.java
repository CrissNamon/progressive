package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

@ComponentScan(value = {"ru.danilarassokhin.progressive.basic.system"})
public class BasicConfiguration extends AbstractConfiguration {

}