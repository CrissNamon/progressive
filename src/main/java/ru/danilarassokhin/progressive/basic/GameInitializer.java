package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.basic.injection.SimplePackageLoader;
import ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.injection.PackageLoader;
import ru.danilarassokhin.progressive.injection.PackageScanner;

/**
 * Initiates important components.
 */
public abstract class GameInitializer {

  /**
   * Initiates game components such as {@link ru.danilarassokhin.progressive.Game}
   * and {@link ru.danilarassokhin.progressive.injection.DIContainer}.
   *
   * @param autoScan if true then scans all available packages for
   *                 {@link ru.danilarassokhin.progressive.annotation.Configuration} classes
   *                 and {@link ru.danilarassokhin.progressive.annotation.GameBean} classes
   */
  public static void init(boolean autoScan) {
    init(new SimplePackageScanner(), new SimplePackageLoader(), autoScan);
  }

  /**
   * Initiates game components such as {@link ru.danilarassokhin.progressive.Game}
   * and {@link ru.danilarassokhin.progressive.injection.DIContainer}.
   *
   * @param packageScanner Scanner to load classes from package
   * @param packageLoader Loader to load available packages
   * @param autoScan if true then scans all available packages for
   *                 {@link ru.danilarassokhin.progressive.annotation.Configuration} classes
   *                 and {@link ru.danilarassokhin.progressive.annotation.GameBean} classes
   */
  public static void init(PackageScanner packageScanner, PackageLoader packageLoader, boolean autoScan) {
    BasicComponentManager
        .getGameLogger()
        .log("", "\n" +
            "╔═╗╦═╗╔═╗╔═╗╦═╗╔═╗╔═╗╔═╗╦╦  ╦╔═╗\n" +
            "╠═╝╠╦╝║ ║║ ╦╠╦╝║╣ ╚═╗╚═╗║╚╗╔╝║╣ \n" +
            "╩  ╩╚═╚═╝╚═╝╩╚═╚═╝╚═╝╚═╝╩ ╚╝ ╚═╝\n");
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
