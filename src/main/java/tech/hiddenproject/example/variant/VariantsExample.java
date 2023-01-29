package tech.hiddenproject.example.variant;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.BasicDIContainer;
import tech.hiddenproject.progressive.injection.SimplePackageScanner;

/**
 * Example of beans variant usage. With variant you can create beans for different platforms, use
 * cases, etc.
 */
public class VariantsExample {

  public VariantsExample() {
    // Create BasicDIContainer instance with android variant
    BasicDIContainer basicDIContainer = new BasicDIContainer("android");
    // Scan package with variant game beans
    basicDIContainer.scanPackage("tech.hiddenproject.example.variant", new SimplePackageScanner());
    // Get bean of type MyService
    MyService myService = basicDIContainer.getBean(MyService.class);
    // Android variant was specified in DI container
    // and in AndroidService, so AndroidService will be used
    myService.printVariant();

    // Create BasicDIContainer instance with default variant
    basicDIContainer = new BasicDIContainer(GameBean.DEFAULT_VARIANT);
    // Scan package with variant game beans
    basicDIContainer.scanPackage("tech.hiddenproject.example.variant", new SimplePackageScanner());
    // Get bean of type MyService
    myService = basicDIContainer.getBean(MyService.class);
    // Default variant was specified in DI container
    // and in WindowsService, so WindowsService will be used
    myService.printVariant();

    // Global service bean has GameBean.GLOBAL_VARIANT, so it always loaded
    GlobalService globalService = basicDIContainer.getBean(GlobalService.class);
    globalService.printVariant();
  }
}
