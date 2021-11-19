package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.log.SimpleGameLogger;
import ru.danilarassokhin.progressive.basic.proxy.BasicProxyCreator;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.log.GameLogger;
import ru.danilarassokhin.progressive.proxy.ProxyCreator;

/**
 * Component manager provides global access to core components:
 * {@link ru.danilarassokhin.progressive.log.GameLogger}, {@link ru.danilarassokhin.progressive.proxy.ProxyCreator},
 * {@link ru.danilarassokhin.progressive.injection.DIContainer}, {@link ru.danilarassokhin.progressive.Game}.
 */
public final class BasicComponentManager {

  private static GameLogger gameLogger;
  private static ProxyCreator proxyCreator;
  private static DIContainer diContainer;
  private static Game game;

  /**
   * Returns {@link ru.danilarassokhin.progressive.log.GameLogger}. If logger hasn't been set,
   * then sets it to {@link ru.danilarassokhin.progressive.basic.log.SimpleGameLogger}.
   *
   * @return Current game logger
   */
  public static GameLogger getGameLogger() {
    if (gameLogger == null) {
      gameLogger = new SimpleGameLogger();
    }
    return gameLogger;
  }

  public static void setGameLogger(GameLogger logger) {
    gameLogger = logger;
  }

  /**
   * Returns {@link ru.danilarassokhin.progressive.proxy.ProxyCreator}. If proxy creator hasn't been set,
   * then sets it to {@link ru.danilarassokhin.progressive.basic.proxy.BasicProxyCreator}.
   *
   * @return Current proxy creator
   */
  public static ProxyCreator getProxyCreator() {
    if (proxyCreator == null) {
      proxyCreator = BasicProxyCreator.getInstance();
    }
    return proxyCreator;
  }

  public static void setProxyCreator(ProxyCreator creator) {
    proxyCreator = creator;
  }

  /**
   * Returns {@link ru.danilarassokhin.progressive.injection.DIContainer}. If di container hasn't been set,
   * then sets it to {@link BasicDIContainer}.
   *
   * @return Current di container
   */
  public static DIContainer getDiContainer() {
    if (diContainer == null) {
      diContainer = new BasicDIContainer();
    }
    return diContainer;
  }

  public static void setDiContainer(DIContainer container) {
    diContainer = container;
  }

  /**
   * Returns {@link ru.danilarassokhin.progressive.Game}. If game hasn't been set,
   * then sets it to {@link ru.danilarassokhin.progressive.basic.BasicGame}.
   *
   * @return Current game
   */
  public static Game getGame() {
    if (game == null) {
      game = new BasicGame();
    }
    return game;
  }

  public static void setGame(Game g) {
    game = g;
  }

}
