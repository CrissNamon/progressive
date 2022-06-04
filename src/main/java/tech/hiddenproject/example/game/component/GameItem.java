package tech.hiddenproject.example.game.component;

import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.component.*;

@GameBean(name = "simpleGameItem")
public class GameItem implements GameComponent {

  private String name;
  private float amount;
  private Long id;

  public GameItem() {}

  @Autofill
  public GameItem(@Qualifier("globalIdGenerator") Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
