package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.component.GameComponent;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

@GameBean(name = "gameItem", policy = GameBeanCreationPolicy.OBJECT)
public class GameItem implements GameComponent {

  private String name;
  private float amount;
  private Long id;

  public GameItem() {
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
