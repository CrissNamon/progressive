package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.isGameScript;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.util.GameComponentInstantiator;

import java.util.HashMap;
import java.util.Map;

@isGameScript
public class NodeSystem extends AbstractGameScript {

    private Map<Long, GameNode> nodes;

    public NodeSystem() {
        nodes = new HashMap<>();
    }

    public <N extends GameNode> N createNode(Class<N> nodeClass) {
        Long lastId = nodes.keySet().stream().max(Long::compareTo).orElse(0L);
        N node = GameComponentInstantiator.instantiate(nodeClass, ++lastId);
        nodes.putIfAbsent(lastId, node);
        return node;
    }

}
