package tech.hiddenproject.example.game;

import tech.hiddenproject.example.game.component.GameItem;
import tech.hiddenproject.progressive.annotation.Configuration;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.annotation.Qualifier;
import tech.hiddenproject.progressive.injection.GameBeanCreationPolicy;
import tech.hiddenproject.progressive.util.BasicObjectCaster;

/**
 * Basic DI configuration.
 */
// Configuration class must be annotated as @Configuration
@Configuration
// Will scan tech.hiddenproject.example.game.component
// package for @GameBean classes and @Configuration classes
// @ComponentScan("tech.hiddenproject.example.game.component")
public class BasicConfiguration {

  private long globalIdGenerator = -1;

  // Will create bean of type BasicObjectCaster and name "objCaster"
  @GameBean(name = "objCaster")
  public BasicObjectCaster objCaster() {
    return new BasicObjectCaster();
  }

  // Will add bean information of type Long and name "globalIdGenerator".
  // Will create new object every time when you call getBean
  // Will give new Long value every time you call getBean("globalIdGenerator", Long.class)
  @GameBean(name = "globalIdGenerator", policy = GameBeanCreationPolicy.OBJECT, order = 1)
  public Long generateId() {
    return ++globalIdGenerator;
  }

  // Use @Qualifier to specify bean name to inject
  @GameBean(name = "generate", order = 3)
  public Long generate(@Qualifier("globalIdGenerator") Long id) {
    return ++globalIdGenerator;
  }

  // Will create bean with type GameItem and name "gameItem"
  // Will inject bean with name "globalIdGenerator" as Long arg
  @GameBean(name = "gameItem", policy = GameBeanCreationPolicy.OBJECT, order = 4)
  public GameItem createItemWithIdGenerator(@Qualifier("generate") Long id) {
    return createItem(id);
  }

  // Will create bean with type GameItem and name "createitem"
  // Before method call will inject random bean of type Long as parameter if such bean exists
  @GameBean(policy = GameBeanCreationPolicy.OBJECT, order = 2)
  public GameItem createItem(@Qualifier("globalIdGenerator") Long id) {
    GameItem item = new GameItem();
    item.setId(id);
    return item;
  }
}
