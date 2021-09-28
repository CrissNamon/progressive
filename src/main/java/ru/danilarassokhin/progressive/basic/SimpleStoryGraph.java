package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.StoryGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class SimpleStoryGraph implements StoryGraph<SimpleStoryNode> {

    private final Map<SimpleStoryNode, List<SimpleStoryNode>> storyGraph;

    public SimpleStoryGraph() {
        storyGraph = new HashMap<>();
    }

    @Override
    public Map<SimpleStoryNode, List<SimpleStoryNode>> getStoryGraph() {
        return storyGraph;
    }

    @Override
    public SimpleStoryNode first() {
        return storyGraph.keySet().stream().findFirst().orElse(null);
    }

    @Override
    public void addNode(SimpleStoryNode... node) {
        for(SimpleStoryNode n : node) {
            storyGraph.putIfAbsent(n, new ArrayList<>());
        }
    }

    @Override
    public void removeNode(SimpleStoryNode node) {
        storyGraph.remove(node);
    }

    @Override
    public void addConnection(SimpleStoryNode from, SimpleStoryNode to) {
        storyGraph.get(from).add(to);
        storyGraph.get(to).add(from);
    }

    @Override
    public void removeConnection(SimpleStoryNode from, SimpleStoryNode to) {
        storyGraph.get(from).remove(to);
        storyGraph.get(to).remove(from);
    }

    public SimpleStoryNode getNodeById(Long id) {
        return storyGraph.keySet()
                .stream().filter(
                        n -> n.getId().equals(id)
        )
        .findFirst().orElse(null);
    }
}
