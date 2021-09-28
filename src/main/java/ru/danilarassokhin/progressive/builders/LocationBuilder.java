package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.data.StoryLocation;

/**
 * Represents builder for location
 * @param <V> Location type
 */
public interface LocationBuilder<V extends StoryLocation> extends Builder<V> {

    /**
     * Sets location name
     * @param name Name to set
     * @return Location builder
     */
    LocationBuilder setName(String name);

    /**
     * Sets restriction to location
     * @param entryRestriction Restriction to set
     * @return Location builder
     */
    LocationBuilder setEntryRestriction(StoryCondition entryRestriction);

}
