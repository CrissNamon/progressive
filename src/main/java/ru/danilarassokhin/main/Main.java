package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.basic.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.component.NodeBundle;
import ru.danilarassokhin.progressive.basic.configuration.BasicConfiguration;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.system.CharacterSystem;
import ru.danilarassokhin.progressive.basic.system.NodeSystem;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;

public class Main {

    public static void main(String[] args) {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        diContainer.loadConfiguration(BasicConfiguration.class);

        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> {
            System.out.println("GAME INITIATED");
        });

        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);

        GameObject nodeFlow = game.addGameObject();

        NodeSystem<NodeBundle> nodeSystem = nodeFlow.getGameScript(NodeSystem.class);

        NodeBundle nodeBundle = new NodeBundle();

        nodeBundle.setText("Hello!");
        GameNode<NodeBundle> node1 = nodeSystem.createNode(GameNode.class);
        node1.setBundle(nodeBundle);
        node1.setBundle(nodeBundle);
        System.out.println(nodeSystem.getNode(node1.getId()).getBundle().getText());

        CharacterSystem characterSystem = nodeFlow.getGameScript(CharacterSystem.class);
        System.out.println(characterSystem.getObjectCaster());
        System.out.println(diContainer.getBean("create", Test.class));

    }
}
