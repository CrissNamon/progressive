package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.component.StorySystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemSystem implements StorySystem<Long, SimpleStoryItem> {

    private final Map<Long, SimpleStoryItem> items;

    protected ItemSystem() {
        items = new HashMap<>();
    }

    @Override
    public Map<Long, SimpleStoryItem> getComponents() {
        return items;
    }

    @Override
    public SimpleStoryItem addComponent(Long id) {
        SimpleStoryItem item = new SimpleStoryItem(id);
        if(items.putIfAbsent(id, item) == null) {
            return item;
        }else{
            return null;
        }
    }

    @Override
    public void removeComponent(Long id) {
        items.remove(id);
    }

    @Override
    public Set<Class<? extends StorySystem>> getRequirements() {
        return null;
    }

    @Override
    public SimpleStoryItem getComponentById(Long id) {
        return items.getOrDefault(id, null);
    }
}
