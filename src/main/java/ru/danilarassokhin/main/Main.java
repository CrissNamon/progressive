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
        //Get DI container instance
        BasicDIContainer diContainer = BasicDIContainer.getInstance();
        //Load configuration class with DI
        BasicDIContainer.getInstance().loadConfiguration(BasicConfiguration.class);
        //Load configuration class with DI and use custom PackageLoader for @ComponentScan
        //You get package name and must return set of classes to load beans from
        BasicDIContainer.getInstance().loadConfiguration(BasicConfiguration.class, (packageName) -> new HashSet<>());

        //State manager instance
        BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
        //Subscribe to some instance. This will be executed when game will be initialized first time
        stateManager.<BasicGame>addListener(GameState.INIT, (g) -> BasicGameLogger.info("GAME INITIATED\n"));

        //Get game instance
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

        //Get bean by it's name and class
        BasicObjectCaster basicObjectCaster = BasicDIContainer.getInstance().getBean("objCaster", BasicObjectCaster.class);
        //Get the first bean of GameItem
        GameItem gameItem = BasicDIContainer.getInstance().getBean(GameItem.class);

        //Start game
        game.start();
        //Stop game
        game.stop();
    }
}
