package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.basic.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.component.NodeBundle;
import ru.danilarassokhin.progressive.basic.configuration.BasicConfiguration;
import ru.danilarassokhin.progressive.basic.system.NodeSystem;
import ru.danilarassokhin.progressive.component.GameObject;

public class Main {

    public static void main(String[] args) {
        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        diContainer.loadConfiguration(BasicConfiguration.class);

        GameObject nodeFlow = game.addGameObject();

        NodeSystem nodeSystem = nodeFlow.getGameScript(NodeSystem.class);

        NodeBundle nodeBundle = new NodeBundle();

        nodeBundle.setText("Hello!");
        GameNode<NodeBundle> node1 = nodeSystem.createNode(GameNode.class);
        node1.setBundle(nodeBundle);
        node1.setBundle(nodeBundle);

    }
}
