package tech.hiddenproject.progressive;

import java.lang.reflect.InvocationTargetException;
import tech.hiddenproject.progressive.basic.log.SimpleGameLogger;
import tech.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import tech.hiddenproject.progressive.exception.GameException;
import tech.hiddenproject.progressive.injection.DIContainer;
import tech.hiddenproject.progressive.log.GameLogger;
import tech.hiddenproject.progressive.manager.GamePublisher;
import tech.hiddenproject.progressive.proxy.ProxyCreator;
import tech.hiddenproject.progressive.util.SimpleComponentCreator;

/**
 * Component manager provides global access to core components: {@link GameLogger},
 * {@link ProxyCreator}, {@link DIContainer}, {@link Game}.
 */
public final class BasicComponentManager {

  private static GameLogger gameLogger;
  private static ProxyCreator proxyCreator;
  private static DIContainer diContainer;
  private static Game game;
  private static ComponentCreator componentCreator;

  private static GamePublisher gamePublisher;

  /**
   * Returns {@link GameLogger}. If logger hasn't been set, then sets it to {@link GameLogger}.
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
   * Returns {@link ProxyCreator}. If proxy creator hasn't been set, then sets it to
   * BasicProxyCreator.
   *
   * @return Current proxy creator
   */
  public static ProxyCreator getProxyCreator() {
    if (proxyCreator == null) {
      throw new GameException("ProxyCreator is not set!");
    }
    return proxyCreator;
  }

  public static void setProxyCreator(ProxyCreator creator) {
    proxyCreator = creator;
  }

  /**
   * Returns {@link DIContainer}. If di container hasn't been set, then sets it to
   * BasicDIContainer.
   *
   * @return Current di container
   */
  public static DIContainer getDiContainer() {
    if (diContainer == null) {
      diContainer = createDIContainer();
    }
    return diContainer;
  }

  public static void setDiContainer(DIContainer container) {
    diContainer = container;
  }

  /**
   * Returns {@link Game}. If game hasn't been set, throws {@link GameException}.
   *
   * @return Current game
   */
  public static Game getGame() {
    if (game == null) {
      throw new GameException("Game component is not set!");
    }
    return game;
  }

  public static void setGame(Game g) {
    game = g;
  }

  public static ComponentCreator getComponentCreator() {
    if (componentCreator == null) {
      componentCreator = createDefaultCreator();
    }
    return componentCreator;
  }

  public static void setComponentCreator(ComponentCreator creator) {
    componentCreator = creator;
  }

  public static <I, S> GamePublisher<I, S> getPublisher() {
    if (gamePublisher == null) {
      gamePublisher = BasicGamePublisher.getInstance();
    }
    return gamePublisher;
  }

  public static void setGamePublisher(GamePublisher publisher) {
    gamePublisher = publisher;
  }

  private static boolean isDiCreatorEnabled() {
    try {
      Class.forName("tech.hiddenproject.progressive.basic.util.BasicComponentCreator",
                    true, BasicComponentManager.class.getClassLoader()
      );
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private static ComponentCreator createDiCreator() {
    try {
      return (ComponentCreator) Class.forName(
              "tech.hiddenproject.progressive.basic.util.BasicComponentCreator")
          .getDeclaredConstructor()
          .newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      throw new GameException("Unable to initialize DI ComponentCreator: " + e.getMessage());
    }
  }

  private static ComponentCreator createDefaultCreator() {
    if (isDiCreatorEnabled()) {
      return createDiCreator();
    }
    return new SimpleComponentCreator();
  }

  public static boolean isDiContainerEnabled() {
    if (diContainer != null) {
      return true;
    }
    try {
      Class.forName("tech.hiddenproject.progressive.basic.BasicDIContainer",
                    true, BasicComponentManager.class.getClassLoader()
      );
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private static DIContainer createDIContainer() {
    if (!isDiContainerEnabled()) {
      throw new GameException(
          "Unable to load component: tech.hiddenproject.progressive.basic.BasicDIContainer");
    }
    try {
      return (DIContainer) Class.forName(
              "tech.hiddenproject.progressive.basic.BasicDIContainer")
          .getDeclaredConstructor()
          .newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      throw new GameException("Unable to initialize DI Container: " + e.getMessage());
    }
  }
}
