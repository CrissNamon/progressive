package ru.danilarassokhin.main.script;

import ru.danilarassokhin.main.component.GameItem;
import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

//Simple Echo script
@IsGameScript //This annotation is required for game scripts
public class EchoSystem implements GameScript {

  //Parent GameObject this script will be attached to
  private GameObject parent;

  //Subscribe to game global update with GamePublisher and pass update delta time to say method
  @Autofill
  public EchoSystem(GameItem gameItem) {
    BasicGamePublisher.getInstance().subscribeOn("update", this::say);
    BasicGameLogger.getInstance().info("GAME ITEM ID: " + gameItem.getId());
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  //This will be called on global game update
  private void say(Object message) {
    System.out.println(message);
  }
}
