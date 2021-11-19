package ru.danilarassokhin.example.injection;

import ru.danilarassokhin.example.game.component.GameItem;
import ru.danilarassokhin.progressive.basic.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameComponent;
import ru.danilarassokhin.progressive.injection.DIContainer;

public class DIContainerExample {

  public DIContainerExample() {
    //Create DI container instance
    DIContainer diContainer = new BasicDIContainer("example");
    //Scan package
    diContainer.scanPackage("ru.danilarassokhin.example.injection", new SimplePackageScanner());

    //Get BasicObjectCaster bean
    BasicObjectCaster basicObjectCaster = diContainer.getBean("objCaster", BasicObjectCaster.class);
    //There is only one bean of type BasicObjectCaster, so name is not required
    basicObjectCaster = diContainer.getBean(BasicObjectCaster.class);

    //Search bean with no BeanNotFoundException
    GameItem notExists = diContainer.searchBean("notExists", GameItem.class).orElse(null);
    assert notExists == null;

    //Get GameItem bean
    GameItem gameItem = diContainer.getBean("gameItem", GameItem.class);
    //Get GameItem bean as interface
    GameComponent gameItemComponent = diContainer.getBean("gameItem", GameComponent.class);
  }
}
