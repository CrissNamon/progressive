package tech.hiddenproject.example.game.script;

import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.component.*;

// Simple Echo script
@IsGameScript
public class EchoSystem implements GameScript {

  // Parent GameObject this script will be attached to
  private GameObject parent;

  // GameItemSystem will be injected from parent game object
  @FromParent private GameItemSystem gameItemSystem;

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

  // This will be called on global game update
  private void say(Object message) {
    System.out.println(message);
  }

  public GameItemSystem getGameItemSystem() {
    return gameItemSystem;
  }
}
