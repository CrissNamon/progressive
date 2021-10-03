package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.configuration.BasicConfiguration;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.system.EchoSystem;
import ru.danilarassokhin.progressive.basic.system.LocationSystem;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        diContainer.loadConfiguration(BasicConfiguration.class);
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> {
            System.out.println("GAME INITIATED");
        });

        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);
        game.setTickRate(100);
        game.setStatic(false);

        GameObject echoObject = game.addGameObject();
        EchoSystem echoSystem = echoObject.getGameScript(EchoSystem.class);
        LocationSystem locationSystem = echoObject.getGameScript(LocationSystem.class);
        game.start();
        String message = " ";
        Scanner sc = new Scanner(System.in);
        while(!message.isEmpty()) {
            System.out.println("ECHO INPUT: ");
            message = sc.nextLine();
            BasicGamePublisher.getInstance().sendTo("EchoInput", message);
        }
        game.stop();
    }
}
