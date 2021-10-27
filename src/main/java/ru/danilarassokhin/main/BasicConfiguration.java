package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.annotation.Components;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

/**
 * Basic DI configuration
 */
//Configuration class must extends AbstractConfiguration
public class BasicConfiguration extends AbstractConfiguration {

  private long globalIdGenerator = -1;

  //Will create bean of type BasicObjectCaster and name "objCaster"
  @GameBean(name = "objCaster")
  public BasicObjectCaster objCaster() {
    return new BasicObjectCaster();
  }

  //Will add bean information of type Long and name "globalIdGenerator".
  //Will create new object every time when you call getBean
  //Will give new Long value every time you call getBean("globalIdGenerator", Long.class)
  @GameBean(name = "globalIdGenerator", policy = GameBeanCreationPolicy.OBJECT)
  public Long generateId() {
    return ++globalIdGenerator;
  }

  //Will create bean with type GameItem and name "createitem"
  //Before method call will inject random bean of type Long as parameter if such bean exists
  @GameBean
  public GameItem createItem(Long id) {
    GameItem item = new GameItem();
    item.setId(id);
    return item;
  }

  //Will create bean with type GameItem and name "gameItem"
  //Will inject bean with name "globalIdGenerator" as Long arg
  @GameBean(name = "gameItem", qualifiers = "globalIdGenerator")
  public GameItem createItemWithIdGenerator(Long id) {
    return createItem(id);
  }

}
