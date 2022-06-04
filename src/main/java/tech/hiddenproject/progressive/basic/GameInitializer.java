package tech.hiddenproject.progressive.basic;

import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.injection.*;
import tech.hiddenproject.progressive.injection.*;

/** Initiates important components. */
public abstract class GameInitializer {

  /**
   * Initiates game components such as {@link Game} and {@link DIContainer}.
   *
   * @param autoScan if true then scans all available packages for {@link Configuration} classes and
   *     {@link GameBean} classes
   */
  public static void init(boolean autoScan) {
    init(new SimplePackageScanner(), new SimplePackageLoader(), autoScan);
  }

  /**
   * Initiates game components such as {@link Game} and {@link DIContainer}.
   *
   * @param packageScanner Scanner to load classes from package
   * @param packageLoader Loader to load available packages
   * @param autoScan if true then scans all available packages for {@link Configuration} classes and
   *     {@link GameBean} classes
   */
  public static void init(
      PackageScanner packageScanner, PackageLoader packageLoader, boolean autoScan) {
    BasicComponentManager.getGameLogger()
        .log(
            "",
            "\n"
                + "╔═╗╦═╗╔═╗╔═╗╦═╗╔═╗╔═╗╔═╗╦╦  ╦╔═╗\n"
                + "╠═╝╠╦╝║ ║║ ╦╠╦╝║╣ ╚═╗╚═╗║╚╗╔╝║╣ \n"
                + "╩  ╩╚═╚═╝╚═╝╩╚═╚═╝╚═╝╚═╝╩ ╚╝ ╚═╝\n");
    BasicComponentManager.getDiContainer();
    BasicComponentManager.getGame();
    if (autoScan) {
      scan(packageScanner, packageLoader);
    }
  }

  /**
   * Scans all packages using {@code packageScanner} and {@code packageLoader}.
   *
   * @param packageScanner Scanner to load classes from package
   * @param packageLoader Loader to load available packages
   */
  private static void scan(PackageScanner packageScanner, PackageLoader packageLoader) {
    DIContainer basicDIContainer = BasicComponentManager.getDiContainer();
    basicDIContainer.init(packageLoader, packageScanner);
  }
}
