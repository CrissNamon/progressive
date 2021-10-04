package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.GameFrameRateType;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.system.EchoSystem;
import ru.danilarassokhin.progressive.manager.GameState;

public class Main {

    public static void main(String[] args) {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> System.out.println("GAME INITIATED"));

        BasicGame game = BasicGame.getInstance();
        game.setGameObjectClass(BasicGameObject.class);
        game.setFrameRate(16);
        game.setStatic(false);
        game.setGameTickRateType(GameFrameRateType.PARALLEL);

        for(int i = 0; i < 1; ++i) {
            game.addGameObject().getGameScript(EchoSystem.class);
        }

        game.start();
        //game.stop();
    }
}
