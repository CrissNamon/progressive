package ru.hiddenproject.progressive.log;

/**
 * Represents abstract game logger
 */
public interface GameLogger {

  String RESET = "\033[0m";  // Text Reset
  String BLACK = "\033[0;30m";   // BLACK
  String RED = "\033[0;31m";     // RED
  String GREEN = "\033[0;32m";   // GREEN
  String YELLOW = "\033[0;33m";  // YELLOW
  String BLUE = "\033[0;34m";    // BLUE
  String PURPLE = "\033[0;35m";  // PURPLE
  String CYAN = "\033[0;36m";    // CYAN
  String WHITE = "\033[0;37m";   // WHITE

  /**
   * Logs {@code message} with {@code prefix}.
   *
   * @param prefix Prefix to use
   * @param message Message to log
   */
  void log(String prefix, Object message);

  /**
   * Logs simple information
   *
   * @param message Message to log.
   */
  void info(Object message);

  /**
   * Logs error information.
   *
   * @param message Error message to log
   */
  void error(Object message);

  /**
   * Logs warning information.
   *
   * @param message Warning message to log
   */
  void warning(Object message);

}
