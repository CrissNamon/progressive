package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

public final class Bean {

    private Object bean;
    private GameBeanCreationPolicy creationPolicy;

    protected Bean(Object bean, GameBeanCreationPolicy creationPolicy) {
        this.bean = bean;
        this.creationPolicy = creationPolicy;
    }

    public Object getBean() {
        return bean;
    }

    public GameBeanCreationPolicy getCreationPolicy() {
        return creationPolicy;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
