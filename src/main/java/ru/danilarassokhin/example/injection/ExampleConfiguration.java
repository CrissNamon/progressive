package ru.danilarassokhin.example.injection;

import ru.danilarassokhin.example.game.component.GameItem;
import ru.danilarassokhin.progressive.annotation.Configuration;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;

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
