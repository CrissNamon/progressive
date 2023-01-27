package tech.hiddenproject.example.injection;

import tech.hiddenproject.example.game.component.GameItem;
import tech.hiddenproject.progressive.annotation.Configuration;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.util.BasicObjectCaster;

@Configuration
public class ExampleConfiguration {

  @GameBean(name = "objCaster", variant = "example")
  public BasicObjectCaster objCaster() {
    return new BasicObjectCaster();
  }

  @GameBean(name = "gameItem", variant = "example")
  public GameItem gameItem() {
    return new GameItem();
  }
}
