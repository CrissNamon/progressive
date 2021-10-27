package ru.danilarassokhin.progressive.basic.util;

import ru.danilarassokhin.progressive.util.GameLogger;

/**
 * Simple logger
 */
public final class BasicGameLogger implements GameLogger {

  private static BasicGameLogger INSTANCE;

  private BasicGameLogger() {}

  public static BasicGameLogger getLogger() {
    if(INSTANCE == null) {
      INSTANCE = new BasicGameLogger();
    }
    return INSTANCE;
  }

  public void log(String prefix, Object message) {
    System.out.println(prefix + " " + message.toString());
  }

  public void info(Object message) {
    log("[PROGRESSIVE INFO]", message);
  }

  public void error(Object message) {
    log(RED + "[PROGRESSIVE ERROR]" + RESET, message);
  }

  public void warning(Object message) {
    log(YELLOW + "[PROGRESSIVE WARN]" + RESET, message);
  }

}
