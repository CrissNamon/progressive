package tech.hiddenproject.example.game;

import java.util.List;
import tech.hiddenproject.example.game.script.EchoSystem;
import tech.hiddenproject.progressive.Game;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.basic.BasicGame;
import tech.hiddenproject.progressive.basic.BasicGameObject;
import tech.hiddenproject.progressive.basic.lambda.StateMachinePersister;
import tech.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.data.StateMachineTransition;
import tech.hiddenproject.progressive.manager.GameEvent;
import tech.hiddenproject.progressive.manager.GameState;

public class GameExample {

  public GameExample() {
    // Subscribe to game events
    BasicGamePublisher.getInstance().subscribeOn(Game.GAME_PUBLISHER_TOPIC, this::processGameEvent);

    // Create game instance with state manager
    Game game = new BasicGame(createPersister());
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
    game.setPreStart(() -> BasicComponentManager.getGameLogger().info("Before start"));
    // Set action to execute after start
    game.setPostStart(() -> BasicComponentManager.getGameLogger().info("After start"));

    // Set action to execute before every update
    game.setPreUpdate(() -> BasicComponentManager.getGameLogger().info("Before update"));
    // Set action to execute after every update
    game.setPostUpdate(() -> BasicComponentManager.getGameLogger().info("After update"));

    // Start game
    game.start();
    // Stop game
    game.stop();
    // Dispose all game objects
    game.dispose();
  }

  // Create test persister for game state
  private StateMachinePersister<GameState, GameEvent, BasicGame> createPersister() {
    return new StateMachinePersister<GameState, GameEvent, BasicGame>() {
      @Override
      public void persist(BasicGame payload,
                          StateMachineTransition<GameState, GameEvent> transition) {
        // Check if next game state is STOPPED, so game is over
        if (transition.getTo().equals(GameState.STOPPED)) {
          BasicComponentManager.getGameLogger().info("Saving game...");
          // Get all game objects, so we can save them somehow
          List<GameObject> allObjects = payload.getAllGameObjects();
        }
      }

      @Override
      public GameState getCurrentState(BasicGame payload) {
        BasicComponentManager.getGameLogger().info("Loading game state...");
        // We can load game state here
        return GameState.UNDEFINED;
      }
    };
  }

  private void processGameEvent(GameEvent event) {
    BasicComponentManager.getGameLogger().info("Catch game event: " + event.getName());
  }
}
