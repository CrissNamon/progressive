package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.component.GameComponent;

public abstract class AbstractGameComponent implements GameComponent {

    private Long id;

    public AbstractGameComponent(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
