package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.builders.ItemBuilder;

public class SimpleItemBuilder implements ItemBuilder<SimpleStoryItem> {

    private SimpleStoryItem item;

    public SimpleItemBuilder(Long id) {
        item = new SimpleStoryItem(id);
    }

    @Override
    public SimpleItemBuilder setStartAmount(float startCount) {
        item.setAmount(startCount);
        return this;
    }

    @Override
    public SimpleItemBuilder setName(String name) {
        item.setName(name);
        return this;
    }

    @Override
    public SimpleStoryItem build() {
        return item;
    }
}
