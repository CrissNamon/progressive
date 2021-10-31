package ru.danilarassokhin.main.script;

import ru.danilarassokhin.main.component.GameItem;
import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.component.GameComponent;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

@IsGameScript //This annotation is required for game scripts
public class GameItemSystem implements GameScript {

  private GameObject parent;

  private GameItem item;

  //Beans can be autofilled as interfaces they implement
  @Autofill(qualifiers = "simpleGameItem")
  public GameItemSystem(GameComponent gameItem) {
    this.item = (GameItem) gameItem;
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  public GameItem getItem() {
    return item;
  }
}