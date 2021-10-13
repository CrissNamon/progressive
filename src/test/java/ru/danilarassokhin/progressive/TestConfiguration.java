package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;

public class TestConfiguration extends AbstractConfiguration {

    @GameBean(name = "TestBean")
    public Long getTestBean() {
        return 1L;
    }

}
