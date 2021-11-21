package ru.hiddenproject.progressive.basic;

import ru.hiddenproject.progressive.Game;
import ru.hiddenproject.progressive.basic.log.SimpleGameLogger;
import ru.hiddenproject.progressive.basic.proxy.BasicProxyCreator;
import ru.hiddenproject.progressive.injection.DIContainer;
import ru.hiddenproject.progressive.log.GameLogger;
import ru.hiddenproject.progressive.proxy.ProxyCreator;

/**
 * Component manager provides global access to core components:
 * {@link ru.hiddenproject.progressive.log.GameLogger}, {@link ru.hiddenproject.progressive.proxy.ProxyCreator},
 * {@link ru.hiddenproject.progressive.injection.DIContainer}, {@link ru.hiddenproject.progressive.Game}.
 */
public final class BasicComponentManager {

  private static GameLogger gameLogger;
  private static ProxyCreator proxyCreator;
  private static DIContainer diContainer;
  private static Game game;

  /**
   * Returns {@link ru.hiddenproject.progressive.log.GameLogger}. If logger hasn't been set,
   * then sets it to {@link ru.hiddenproject.progressive.basic.log.SimpleGameLogger}.
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
   * Returns {@link ru.hiddenproject.progressive.proxy.ProxyCreator}. If proxy creator hasn't been set,
   * then sets it to {@link ru.hiddenproject.progressive.basic.proxy.BasicProxyCreator}.
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
   * Returns {@link ru.hiddenproject.progressive.injection.DIContainer}. If di container hasn't been set,
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
   * Returns {@link ru.hiddenproject.progressive.Game}. If game hasn't been set,
   * then sets it to {@link ru.hiddenproject.progressive.basic.BasicGame}.
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
