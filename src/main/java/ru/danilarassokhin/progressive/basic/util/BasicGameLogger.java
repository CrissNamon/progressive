package ru.danilarassokhin.progressive.basic.util;

import ru.danilarassokhin.progressive.basic.log.SimpleGameLogger;
import ru.danilarassokhin.progressive.util.GameLogger;

/**
 * Global game logger.
 */
public final class BasicGameLogger {

  private static BasicGameLogger INSTANCE;

  private GameLogger gameLogger;

  private BasicGameLogger() {
    gameLogger = new SimpleGameLogger();
  }

  public static BasicGameLogger getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new BasicGameLogger();
    }
    return INSTANCE;
  }

  /**
   * Returns current logger
   *
   * @return {@link ru.danilarassokhin.progressive.util.GameLogger}
   */
  public GameLogger getLogger() {
    return gameLogger;
  }

  /**
   * Sets logger.
   *
   * @param gameLogger {@link ru.danilarassokhin.progressive.util.GameLogger} to use
   */
  public void setLogger(GameLogger gameLogger) {
    this.gameLogger = gameLogger;
  }

  /**
   * Logs {@code message} with {@code prefix} using current logger.
   *
   * @param prefix Prefix to use
   * @param message Message to log
   */
  public void log(String prefix, Object message) {
    gameLogger.log(prefix, message);
  }

  /**
   * Logs simple information using current logger.
   *
   * @param message Message to log
   */
  public void info(Object message) {
    gameLogger.info(message);
  }

  /**
   * Logs error information using current logger.
   *
   * @param message Error message to log
   */
  public void error(Object message) {
    gameLogger.error(message);
  }

  /**
   * Logs warning information using current logger.
   *
   * @param message Warning message to log
   */
  public void warning(Object message) {
    gameLogger.warning(message);
  }

}
