package tech.hiddenproject.example.injection;

import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.basic.injection.*;
import tech.hiddenproject.progressive.basic.util.*;
import tech.hiddenproject.progressive.component.*;
import tech.hiddenproject.progressive.injection.*;

public class DIContainerExample {

  public DIContainerExample() {
    // Create DI container instance
    DIContainer diContainer = new BasicDIContainer("example");
    // Scan package
    diContainer.scanPackage("tech.hiddenproject.example.injection", new SimplePackageScanner());

    // Get BasicObjectCaster bean
    BasicObjectCaster basicObjectCaster = diContainer.getBean("objCaster", BasicObjectCaster.class);
    // There is only one bean of type BasicObjectCaster, so name is not required
    basicObjectCaster = diContainer.getBean(BasicObjectCaster.class);

    // Search bean with no BeanNotFoundException
    GameItem notExists = diContainer.searchBean("notExists", GameItem.class).orElse(null);
    assert notExists == null;

    // Get GameItem bean
    GameItem gameItem = diContainer.getBean("gameItem", GameItem.class);
    // Get GameItem bean as interface
    GameComponent gameItemComponent = diContainer.getBean("gameItem", GameComponent.class);
  }
}
