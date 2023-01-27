package tech.hiddenproject.progressive.basic.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.hiddenproject.progressive.log.GameLogger;

/**
 * Simple game logger implementation using {@link java.lang.System}.
 */
public class SimpleGameLogger implements GameLogger {

  private static final Logger log = LoggerFactory.getLogger(SimpleGameLogger.class);

  @Override
  public void log(String prefix, Object message) {
    log.info(prefix + " " + message);
  }

  @Override
  public void info(Object message) {
    log("[PROGRESSIVE INFO]", message);
  }

  @Override
  public void error(Object message) {
    log(RED + "[PROGRESSIVE ERROR]" + RESET, message);
  }

  @Override
  public void warning(Object message) {
    log(YELLOW + "[PROGRESSIVE WARN]" + RESET, message);
  }
}
