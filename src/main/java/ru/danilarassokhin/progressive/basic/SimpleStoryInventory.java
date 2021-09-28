package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.data.StoryInventory;

import java.util.*;

public class SimpleStoryInventory implements StoryInventory<SimpleStoryItem> {

    private final Set<SimpleStoryItem> items;

    protected SimpleStoryInventory() {
        this.items = new HashSet<>();
    }

    protected SimpleStoryInventory(Set<SimpleStoryItem> items) {
        this.items = items;
    }

    public SimpleStoryInventory(SimpleStoryItem... items) {
        this.items = new HashSet<>();
        this.items.addAll(Arrays.asList(items));
    }

    @Override
    public boolean addItem(SimpleStoryItem item) {
        return items.add(
                new SimpleStoryItem(
                        item.getId(),
                        item.getName(),
                        item.getCount()
                )
        );
    }

    @Override
    public boolean hasItem(SimpleStoryItem item) {
        return items.contains(item);
    }

    @Override
    public void removeItem(SimpleStoryItem item) {
        items.remove(item);
    }

    @Override
    public Set<SimpleStoryItem> getItems() {
        return items;
    }

    @Override
    public void addItem(SimpleStoryItem item, float add) {
        items.stream().filter(i -> i.getId().equals(item.getId()))
                .findFirst().ifPresentOrElse(i -> i.add(add), () -> {});
    }
}
