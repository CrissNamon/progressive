package tech.hiddenproject.progressive.basic.log;

import tech.hiddenproject.progressive.log.*;

/** Simple game logger implementation using {@link java.lang.System}. */
public class SimpleGameLogger implements GameLogger {

  @Override
  public void log(String prefix, Object message) {
    System.out.println(prefix + " " + message.toString());
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
