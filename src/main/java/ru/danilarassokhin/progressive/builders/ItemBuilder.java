package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.data.StoryItem;

/**
 * Represents item builder
 * @param <V> Item type
 */
public interface ItemBuilder<V extends StoryItem> extends Builder<V>{

    /**
     * @param amount Start amount of item
     * @return Item builder
     */
    ItemBuilder setStartAmount(float amount);

    ItemBuilder setName(String name);


}
