package ru.danilarassokhin.progressive.basic.configuration;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

public class BasicConfiguration extends AbstractConfiguration {

    private long globalIdGenerator = -1;

    @GameBean(name = "objCaster")
    public BasicObjectCaster objCaster() {
        return new BasicObjectCaster();
    }

    @GameBean(name = "globalIdGenerator", policy = GameBeanCreationPolicy.OBJECT)
    public Long generateId() {
        return ++globalIdGenerator;
    }

}
