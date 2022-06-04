package tech.hiddenproject.example.injection;

import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.util.*;

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
