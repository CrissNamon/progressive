package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.annotation.GameBean;

@GameBean(name = "globalIdGenerator")
public final class GlobalIdGenerator {

    private long generator;

    public Long next() {
        return ++generator;
    }

}
