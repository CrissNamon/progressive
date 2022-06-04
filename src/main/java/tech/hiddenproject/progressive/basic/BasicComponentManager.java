package tech.hiddenproject.progressive.basic;

import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.basic.log.*;
import tech.hiddenproject.progressive.basic.proxy.*;
import tech.hiddenproject.progressive.injection.*;
import tech.hiddenproject.progressive.log.*;
import tech.hiddenproject.progressive.proxy.*;

/**
 * Component manager provides global access to core components: {@link GameLogger}, {@link
 * ProxyCreator}, {@link DIContainer}, {@link Game}.
 */
public final class BasicComponentManager {

  private static GameLogger gameLogger;
  private static ProxyCreator proxyCreator;
  private static DIContainer diContainer;
  private static Game game;

  /**
   * Returns {@link GameLogger}. If logger hasn't been set, then sets it to {@link
   * SimpleGameLogger}.
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
   * Returns {@link ProxyCreator}. If proxy creator hasn't been set, then sets it to {@link
   * BasicProxyCreator}.
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
   * Returns {@link DIContainer}. If di container hasn't been set, then sets it to {@link
   * BasicDIContainer}.
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
   * Returns {@link Game}. If game hasn't been set, then sets it to {@link BasicGame}.
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
