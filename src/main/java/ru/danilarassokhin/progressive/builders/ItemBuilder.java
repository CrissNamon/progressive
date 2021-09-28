package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.data.StoryItem;

public interface ItemBuilder<V extends StoryItem> extends Builder<V>{

    <B extends ItemBuilder<V>> B setStartCount(float startCount);

    <B extends ItemBuilder<V>> B setName(String name);


}
