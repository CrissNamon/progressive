package tech.hiddenproject.example.game.script;

import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.FromParent;
import tech.hiddenproject.progressive.annotation.IsGameScript;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.component.GameScript;

// Simple Echo script
@IsGameScript
public class EchoSystem implements GameScript {

  // Parent GameObject this script will be attached to
  private GameObject parent;

  // GameItemSystem will be injected from parent game object
  @FromParent
  private GameItemSystem gameItemSystem;

  @Override
  public void start() {
    BasicComponentManager.getGameLogger().info("EchoSystem start");
  }

  @Override
  public void dispose() {
    BasicComponentManager.getGameLogger().info("EchoSystem dispose");
  }

  @Override
  public void update(long delta) {
    say(delta);
  }

  @Override
  public void stop() {
    BasicComponentManager.getGameLogger().info("EchoSystem stop");
  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  public GameItemSystem getGameItemSystem() {
    return gameItemSystem;
  }

  // This will be called on global game update
  private void say(Object message) {
    BasicComponentManager.getGameLogger().info(message);
  }
}
