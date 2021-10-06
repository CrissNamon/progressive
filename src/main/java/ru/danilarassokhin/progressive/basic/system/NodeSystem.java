package ru.danilarassokhin.progressive.basic.system;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.component.NodeBundle;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;

import java.util.HashMap;
import java.util.Map;

@IsGameScript
public class NodeSystem<B extends NodeBundle>  {

    private Map<Long, GameNode<B>> nodes;

    public NodeSystem(BasicGameObject parent) {

        nodes = new HashMap<>();
        BasicGamePublisher.getInstance().<Long>subscribeOn("NodeSystemInput", (d) -> System.out.println("OUTPUT IN NODESYSTEM: " + d));
    }

    public <N extends GameNode<B>> N createNode(Class<N> nodeClass) {
        Long lastId = nodes.keySet().stream().max(Long::compareTo).orElse(0L);
        N node = BasicDIContainer.create(nodeClass, ++lastId);
        nodes.putIfAbsent(lastId, node);
        return node;
    }

    public GameNode<B> getNode(Long id) {
        return nodes.getOrDefault(id, null);
    }

    protected void start() {
        System.out.println("NODE SYSTEM START");
    }

    protected void update() {

    }
}
