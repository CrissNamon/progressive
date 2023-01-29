package tech.hiddenproject.progressive.injection.data;

import tech.hiddenproject.progressive.annotation.IsGameScript;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.component.GameScript;

/**
 * @author Danila Rassokhin
 */
@IsGameScript
public class TestScript implements GameScript {

  private GameObject parent;

  private int startCounter;
  private int updateCounter;
  private int stopCounter;
  private int disposeCounter;
  private Long delta;

  @Override
  public void start() {
    startCounter++;
  }

  @Override
  public void dispose() {
    disposeCounter++;
  }

  @Override
  public void update(long delta) {
    updateCounter++;
    this.delta = delta;
  }

  @Override
  public void stop() {
    stopCounter++;
  }

  @Override
  public void setGameObject(GameObject parent) {
    this.parent = parent;
  }

  @Override
  public GameObject gameObject() {
    return parent;
  }

  public GameObject getParent() {
    return parent;
  }

  public int getStartCounter() {
    return startCounter;
  }

  public int getUpdateCounter() {
    return updateCounter;
  }

  public int getStopCounter() {
    return stopCounter;
  }

  public int getDisposeCounter() {
    return disposeCounter;
  }

  public Long getDelta() {
    return delta;
  }
}
