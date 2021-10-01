package ru.danilarassokhin.progressive.basic.component;

public class GameNode<B extends NodeBundle> extends AbstractGameComponent {

    private B bundle;

    public GameNode(Long id) {
        super(id);
    }

    public B getBundle() {
        return bundle;
    }

    public void setBundle(B bundle) {
        this.bundle = bundle;
    }
}
