package tech.hiddenproject.example.game.script;

import tech.hiddenproject.progressive.annotation.IsGameScript;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.component.GameScript;

@IsGameScript
public class CharacterSystem implements GameScript {

  private GameObject parent;

  private float health;

  private CharacterSystem() {
    health = 100;
  }

  public float getHealth() {
    return health;
  }

  public void setHealth(float health) {
    this.health = health;
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

  @Override
  public GameObject gameObject() {
    return parent;
  }
}
