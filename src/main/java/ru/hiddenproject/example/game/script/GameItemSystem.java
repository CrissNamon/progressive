package ru.hiddenproject.example.game.script;

import ru.hiddenproject.example.game.component.GameItem;
import ru.hiddenproject.progressive.annotation.Autofill;
import ru.hiddenproject.progressive.annotation.IsGameScript;
import ru.hiddenproject.progressive.annotation.Qualifier;
import ru.hiddenproject.progressive.component.GameComponent;
import ru.hiddenproject.progressive.component.GameObject;
import ru.hiddenproject.progressive.component.GameScript;

@IsGameScript //This annotation is required for game scripts
public class GameItemSystem implements GameScript {

  private GameObject parent;

  private final GameItem item;

  //Beans can be autofilled as interfaces they implement
  @Autofill
  public GameItemSystem(@Qualifier("gameItem") GameComponent gameItem) {
    this.item = (GameItem) gameItem;
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  @Override
  public void start() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void update(long delta) {

  }

  @Override
  public void stop() {

  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  public GameItem getItem() {
    return item;
  }
}
