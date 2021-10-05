package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.GameFrameTimeType;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.system.EchoSystem;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.manager.GameState;

public class Main {

    public static void main(String[] args) {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> BasicGameLogger.info("GAME INITIATED\n"));

        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);
        game.setFrameTime(16);
        game.setStatic(false);
        game.setFrameTimeType(GameFrameTimeType.SEQUENCE);

        for(int i = 0; i < 100; ++i) {
            game.addGameObject().getGameScript(EchoSystem.class);
            game.addGameObject().getGameScript(EchoSystem.class);
            game.addGameObject().getGameScript(EchoSystem.class);
        }

        game.start();
        //game.stop();
    }
}
