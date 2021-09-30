package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.system.StorySystem;

import java.util.HashMap;
import java.util.Map;

public class LocationSystem implements StorySystem<Long, SimpleStoryLocation> {
    private final Map<Long, SimpleStoryLocation> locations;

    protected  LocationSystem() {
        locations = new HashMap<>();
    }

    @Override
    public Map<Long, SimpleStoryLocation> getComponents() {
        return locations;
    }

    @Override
    public SimpleStoryLocation addComponent(Long id) {
        SimpleStoryLocation location = new SimpleStoryLocation(id);
        if(locations.putIfAbsent(id, location) == null) {
            return location;
        }else{
            return null;
        }
    }

    @Override
    public void removeComponent(Long id) {
        locations.remove(id);
    }

    @Override
    public SimpleStoryLocation getComponentById(Long id) {
        return locations.getOrDefault(id, null);
    }
}
