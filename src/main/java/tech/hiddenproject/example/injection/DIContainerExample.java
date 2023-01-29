package tech.hiddenproject.example.injection;

import tech.hiddenproject.example.game.component.GameItem;
import tech.hiddenproject.progressive.basic.BasicDIContainer;
import tech.hiddenproject.progressive.component.GameComponent;
import tech.hiddenproject.progressive.injection.DIContainer;
import tech.hiddenproject.progressive.injection.SimplePackageScanner;
import tech.hiddenproject.progressive.util.BasicObjectCaster;

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
