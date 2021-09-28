package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryItem;

import java.io.Serializable;

public class SimpleStoryItem implements StoryItem<Long>, Serializable {

    private final Long id;
    private String name;
    private float count;

    protected SimpleStoryItem(Long id, String name, float startCount) {
        this.id = id;
        this.name = name;
        this.count = startCount;
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
}
