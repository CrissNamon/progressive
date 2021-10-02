package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.component.NodeBundle;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.util.HashMap;
import java.util.Map;

@IsGameScript
public class NodeSystem<B extends NodeBundle> extends AbstractGameScript {

    private Map<Long, GameNode<B>> nodes;

    public NodeSystem() {
        nodes = new HashMap<>();
    }

    public <N extends GameNode<B>> N createNode(Class<N> nodeClass) {
        Long lastId = nodes.keySet().stream().max(Long::compareTo).orElse(0L);
        N node = ComponentCreator.create(nodeClass, ++lastId);
        nodes.putIfAbsent(lastId, node);
        return node;
    }

    public GameNode<B> getNode(Long id) {
        return nodes.getOrDefault(id, null);
    }

}
