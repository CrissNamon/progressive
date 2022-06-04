package tech.hiddenproject.example.game.script;

import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.component.*;

@IsGameScript // This annotation is required for game scripts
public class GameItemSystem implements GameScript {

  private final GameItem item;
  private GameObject parent;

  // Beans can be autofilled as interfaces they implement
  @Autofill
  public GameItemSystem(@Qualifier("gameItem") GameComponent gameItem) {
    this.item = (GameItem) gameItem;
  }

  @Override
  public void start() {}

  @Override
  public void dispose() {}

  @Override
  public void update(long delta) {}

  @Override
  public void stop() {}

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  public GameItem getItem() {
    return item;
  }
}
