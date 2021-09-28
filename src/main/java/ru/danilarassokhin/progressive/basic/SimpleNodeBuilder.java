package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.builders.NodeBuilder;

public class SimpleNodeBuilder implements NodeBuilder<SimpleStoryNode> {

    private SimpleStoryNode node;

    public SimpleNodeBuilder(Long id, String content) {
        node = new SimpleStoryNode(id, content);
    }

    @Override
    public SimpleStoryNode build() {
        return node;
    }

}
