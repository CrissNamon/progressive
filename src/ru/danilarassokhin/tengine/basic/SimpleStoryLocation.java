package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryCondition;
import ru.danilarassokhin.tengine.StoryLocation;

public class SimpleStoryLocation implements StoryLocation<Long> {

    private final Long id;
    private String name;
    private StoryCondition entryRestriction;

    protected SimpleStoryLocation(Long id, String name) {
        this.id = id;
        this.name = name;
        this.entryRestriction = () -> true;
    }

    protected SimpleStoryLocation(Long id, String name, StoryCondition entryRestriction) {
        this.id = id;
        this.name = name;
        this.entryRestriction = entryRestriction;
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
    public boolean canEntry() {
        return entryRestriction.isTrue();
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
