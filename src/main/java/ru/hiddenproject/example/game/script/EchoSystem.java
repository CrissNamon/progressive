package ru.hiddenproject.example.game.script;

import ru.hiddenproject.progressive.annotation.FromParent;
import ru.hiddenproject.progressive.annotation.IsGameScript;
import ru.hiddenproject.progressive.basic.BasicComponentManager;
import ru.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import ru.hiddenproject.progressive.component.GameObject;
import ru.hiddenproject.progressive.component.GameScript;

//Simple Echo script
@IsGameScript
public class EchoSystem implements GameScript {

  //Parent GameObject this script will be attached to
  private GameObject parent;

  //GameItemSystem will be injected from parent game object
  @FromParent
  private GameItemSystem gameItemSystem;

  //Subscribe to game global update with GamePublisher and pass update delta time to say method
  public EchoSystem() {
    BasicGamePublisher.getInstance().subscribeOn("update", this::say);
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  @Override
  public void start() {
    BasicComponentManager
        .getGameLogger().info("EchoSystem start");
  }

  @Override
  public void dispose() {
    BasicComponentManager
        .getGameLogger().info("EchoSystem dispose");
  }

  @Override
  public void update(long delta) {
    say(delta);
  }

  @Override
  public void stop() {
    BasicComponentManager
        .getGameLogger().info("EchoSystem stop");
  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  //This will be called on global game update
  private void say(Object message) {
    System.out.println(message);
  }

  public GameItemSystem getGameItemSystem() {
    return gameItemSystem;
  }
}
