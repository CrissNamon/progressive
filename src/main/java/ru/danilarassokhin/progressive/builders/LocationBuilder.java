package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.data.StoryLocation;

public interface LocationBuilder<V extends StoryLocation> extends Builder<V> {

    <B extends LocationBuilder<V>> B setName(String name);

    <B extends LocationBuilder<V>> B setEntryRestriction(StoryCondition entryRestriction);

}
