package ru.danilarassokhin.main.script;

import ru.danilarassokhin.main.component.GameItem;
import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

@IsGameScript //This annotation is required for game scripts
public class GameItemSystem implements GameScript {

  private GameObject parent;

  private GameItem item;

  @Autofill
  public GameItemSystem(GameItem gameItem) {
    this.item = gameItem;
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
