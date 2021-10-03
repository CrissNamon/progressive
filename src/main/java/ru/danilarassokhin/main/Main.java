package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.GameTickRateType;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.system.CharacterSystem;
import ru.danilarassokhin.progressive.manager.GameState;

public class Main {

    public static void main(String[] args) throws Throwable {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> {
            System.out.println("GAME INITIATED");
        });

        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);
        game.setTickRate(17);
        game.setStatic(false);

        BasicGameObject echoObject = (BasicGameObject) game.addGameObject();
        CharacterSystem characterSystem = echoObject.getGameScript(CharacterSystem.class);
        Long start = System.currentTimeMillis();
        game.setGameTickRateType(GameTickRateType.PARALLEL);
        game.start();
        game.stop();
        System.out.println((System.currentTimeMillis() - start)/1000);
    }
}
