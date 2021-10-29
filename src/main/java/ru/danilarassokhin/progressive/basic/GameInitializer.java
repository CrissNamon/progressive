package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;

/**
 * Initiates important components.
 */
public abstract class GameInitializer {
  /**
   * Initiates game components such as {@link ru.danilarassokhin.progressive.Game}
   * and {@link ru.danilarassokhin.progressive.injection.DIContainer}.
   *
   * @param args Command line args
   */
  public static void init(String[] args) {
    BasicGameLogger.getInstance()
        .log("", "\n" +
            "╔═╗╦═╗╔═╗╔═╗╦═╗╔═╗╔═╗╔═╗╦╦  ╦╔═╗\n" +
            "╠═╝╠╦╝║ ║║ ╦╠╦╝║╣ ╚═╗╚═╗║╚╗╔╝║╣ \n" +
            "╩  ╩╚═╚═╝╚═╝╩╚═╚═╝╚═╝╚═╝╩ ╚╝ ╚═╝\n");
    BasicGame.createInstance();
    BasicDIContainer.createInstance();
  }
}
