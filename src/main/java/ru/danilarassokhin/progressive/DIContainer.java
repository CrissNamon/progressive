package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

public interface DIContainer {

    public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config);

    public <V> V getBean(String name, Class<V> beanClass);

    public <V> V getBean(Class<V> beanClass);

}
