package tech.hiddenproject.example.game;

import tech.hiddenproject.example.game.script.*;
import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.basic.manager.*;
import tech.hiddenproject.progressive.manager.*;

public class GameExample {

  public GameExample() {
    // Initiate game components such as DI container and Game
    // autoScan = true means Scan all packages for beans and configurations classes
    GameInitializer.init(true);

    // State manager instance
    BasicGameStateManager stateManager = new BasicGameStateManager();
    // Subscribe to some instance. This will be executed when game will be initialized first time
    stateManager.<BasicGame>addListener(
        GameState.INIT, (g) -> BasicComponentManager.getGameLogger().info("GAME INITIATED\n"));

    // Create game instance with state manager
    Game game = new BasicGame(stateManager);
    // Put game instance to component manager for global access
    BasicComponentManager.setGame(game);

    // Get game instance
    game = BasicComponentManager.getGame();
    game.setGameObjectClass(BasicGameObject.class);
    game.setFrameTime(16);
    game.setStatic(false);

    for (int i = 0; i < 100; ++i) {
      // Add game object and attach EchoSystem
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
    }

    GameScene gameScene = new GameScene();
    gameScene.loadScene();

    // Set action to execute before start
    game.setPreStart(() -> System.out.println("Before start"));
    // Set action to execute after start
    game.setPostStart(() -> System.out.println("After start"));

    // Set action to execute before every update
    game.setPreUpdate(() -> System.out.println("Before update"));
    // Set action to execute after every update
    game.setPostUpdate(() -> System.out.println("After update"));

    // Start game
    game.start();
    // Stop game
    game.stop();
    // Dispose all game objects
    game.dispose();
  }
}
