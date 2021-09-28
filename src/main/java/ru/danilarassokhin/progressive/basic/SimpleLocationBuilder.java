package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.builders.LocationBuilder;
import ru.danilarassokhin.progressive.lambdas.StoryCondition;

public class SimpleLocationBuilder implements LocationBuilder<SimpleStoryLocation> {

    private SimpleStoryLocation location;

    public SimpleLocationBuilder(Long id) {
        location = new SimpleStoryLocation(id);
    }

    @Override
    public SimpleLocationBuilder setName(String name) {
        location.setName(name);
        return this;
    }

    @Override
    public SimpleLocationBuilder setEntryRestriction(StoryCondition entryRestriction) {
        location.setEntryRestriction(entryRestriction);
        return this;
    }

    @Override
    public SimpleStoryLocation build() {
        return location;
    }
}
