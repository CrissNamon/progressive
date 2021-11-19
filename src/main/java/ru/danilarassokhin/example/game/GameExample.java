package ru.danilarassokhin.example.game;

import ru.danilarassokhin.example.game.script.EchoSystem;
import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.PublisherType;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.GameInitializer;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.manager.GameState;

public class GameExample {

  public GameExample() {
    //Initiate game components such as DI container and Game
    //autoScan = true means Scan all packages for beans and configurations classes
    GameInitializer.init(true);
    //Get DI container instance
    DIContainer diContainer = BasicComponentManager.getDiContainer();
    //State manager instance
    BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
    //Subscribe to some instance. This will be executed when game will be initialized first time
    stateManager.<BasicGame>addListener(GameState.INIT, (g) -> BasicComponentManager
        .getGameLogger().info("GAME INITIATED\n"));
    //Get game instance
    Game game = BasicComponentManager.getGame();
    game.setGameObjectClass(BasicGameObject.class);
    game.setFrameTime(16);
    game.setStatic(false);

    for (int i = 0; i < 100; ++i) {
      //Add game object and attach EchoSystem
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
    }

    GameScene gameScene = new GameScene();
    gameScene.loadScene();

    //Start game
    game.start();
    //Stop game
    game.stop();
    //Dispose all game objects
    game.dispose();
  }
}
