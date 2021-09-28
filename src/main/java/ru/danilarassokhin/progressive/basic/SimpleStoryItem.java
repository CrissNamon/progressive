package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.data.StoryItem;

import java.io.Serializable;

public class SimpleStoryItem implements StoryItem<Long>, Serializable, AutoCloseable {

    private final Long id;
    private String name;
    private float count;

    protected SimpleStoryItem(Long id, String name, float startCount) {
        this.id = id;
        this.name = name;
        this.count = startCount;
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
    public float getCount() {
        return count;
    }

    @Override
    public void setCount(float count) {
        this.count = count;
    }

    @Override
    public void add(float add) {
        this.count += add;
    }

    @Override
    public void close() throws ClassCastException {

    }
}
