package ru.danilarassokhin.example.game.script;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

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
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

}
