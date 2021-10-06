package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.GameFrameTimeType;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.basic.configuration.BasicConfiguration;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.script.EchoSystem;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.manager.GameState;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> BasicGameLogger.info("GAME INITIATED\n"));

        BasicDIContainer.getInstance().loadConfiguration(BasicConfiguration.class, (name) -> {

            return new HashSet<>();
        });

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

        BasicObjectCaster basicObjectCaster = BasicDIContainer.getInstance().getBean("objCaster", BasicObjectCaster.class);
        GameItem gameItem = BasicDIContainer.getInstance().getBean(GameItem.class);

        game.start();
        game.stop();
    }
}
