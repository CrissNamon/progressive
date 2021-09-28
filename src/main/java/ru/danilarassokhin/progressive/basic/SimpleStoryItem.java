package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.data.StoryItem;

import java.io.Serializable;

public class SimpleStoryItem implements StoryItem<Long>, Serializable, AutoCloseable {

    private final Long id;
    private String name;
    private float amount;

    protected SimpleStoryItem(Long id, String name, float startAmount) {
        this.id = id;
        this.name = name;
        this.amount = startAmount;
    }

    protected SimpleStoryItem(Long id) {
        this.id = id;
        this.name = "";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setAmount(float count) {
        this.amount = count;
    }

    @Override
    public void add(float add) {
        this.amount += add;
    }

    @Override
    public void close() throws ClassCastException {

    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
