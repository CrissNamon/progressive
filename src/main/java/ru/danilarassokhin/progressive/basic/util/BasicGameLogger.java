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

  public GameLogger getLogger() {
    return gameLogger;
  }

  public void setLogger(GameLogger gameLogger) {
    this.gameLogger = gameLogger;
  }

  public void log(String prefix, Object message) {
    gameLogger.log(prefix, message);
  }

  public void info(Object message) {
    gameLogger.info(message);
  }

  public void error(Object message) {
    gameLogger.error(message);
  }

  public void warning(Object message) {
    gameLogger.warning(message);
  }

}
