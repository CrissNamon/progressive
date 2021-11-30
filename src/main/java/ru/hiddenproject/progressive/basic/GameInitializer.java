package ru.hiddenproject.progressive.basic;

import ru.hiddenproject.progressive.basic.injection.SimplePackageLoader;
import ru.hiddenproject.progressive.basic.injection.SimplePackageScanner;
import ru.hiddenproject.progressive.injection.DIContainer;
import ru.hiddenproject.progressive.injection.PackageLoader;
import ru.hiddenproject.progressive.injection.PackageScanner;

/**
 * Initiates important components.
 */
public abstract class GameInitializer {

  /**
   * Initiates game components such as {@link ru.hiddenproject.progressive.Game}
   * and {@link ru.hiddenproject.progressive.injection.DIContainer}.
   *
   * @param autoScan if true then scans all available packages for
   *                 {@link ru.hiddenproject.progressive.annotation.Configuration} classes
   *                 and {@link ru.hiddenproject.progressive.annotation.GameBean} classes
   */
  public static void init(boolean autoScan) {
    init(new SimplePackageScanner(), new SimplePackageLoader(), autoScan);
  }

  /**
   * Initiates game components such as {@link ru.hiddenproject.progressive.Game}
   * and {@link ru.hiddenproject.progressive.injection.DIContainer}.
   *
   * @param packageScanner Scanner to load classes from package
   * @param packageLoader  Loader to load available packages
   * @param autoScan       if true then scans all available packages for
   *                       {@link ru.hiddenproject.progressive.annotation.Configuration} classes
   *                       and {@link ru.hiddenproject.progressive.annotation.GameBean} classes
   */
  public static void init(PackageScanner packageScanner, PackageLoader packageLoader, boolean autoScan) {
    BasicComponentManager
        .getGameLogger()
        .log("", "\n"
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
   * @param packageLoader  Loader to load available packages
   */
  private static void scan(PackageScanner packageScanner, PackageLoader packageLoader) {
    DIContainer basicDIContainer = BasicComponentManager.getDiContainer();
    basicDIContainer.init(packageLoader, packageScanner);
  }

}
