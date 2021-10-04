package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.component.GameComponent;

public class GameNode<B extends NodeBundle> implements GameComponent {

    private B bundle;
    private Long id;

    public B getBundle() {
        return bundle;
    }

    public void setBundle(B bundle) {
        this.bundle = bundle;
    }

    @Override
    public Long getId() {
        return id;
    }
}
