package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.data.StoryNode;

/**
 * Represents builder for nodes
 * @param <V> Node type
 */
public interface NodeBuilder<V extends StoryNode> extends Builder<V> {

}
