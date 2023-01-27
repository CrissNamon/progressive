package tech.hiddenproject.progressive.manager;

import java.util.Objects;

/**
 * @author Danila Rassokhin
 */
public class GameEvent {

  public static final GameEvent INITIALIZATION = new GameEvent("INITIALIZATION");
  public static final GameEvent START = new GameEvent("START");
  public static final GameEvent PLAY = new GameEvent("PLAY");
  public static final GameEvent STOP = new GameEvent("STOP");

  public static final GameEvent NEW_OBJECT = new GameEvent("NEW_OBJECT");
  public static final GameEvent REMOVE_OBJECT = new GameEvent("REMOVE_OBJECT");
  public static final GameEvent GLOBAL_DISPOSE = new GameEvent("GLOBAL_DISPOSE");

  private final String name;

  private Object payload;

  public GameEvent(String name, Object payload) {
    this.name = name;
    this.payload = payload;
  }

  public GameEvent(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public <P> P getPayload() {
    return (P) payload;
  }

  public GameEvent setPayload(Object payload) {
    this.payload = payload;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameEvent gameEvent = (GameEvent) o;
    return getName().equals(gameEvent.getName());
  }

  @Override
  public String toString() {
    return "GameEvent{" +
        "name='" + name + '\'' +
        '}';
  }
}
