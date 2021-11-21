package ru.hiddenproject.example.injection;

import ru.hiddenproject.example.game.component.GameItem;
import ru.hiddenproject.progressive.annotation.Configuration;
import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.util.BasicObjectCaster;

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
