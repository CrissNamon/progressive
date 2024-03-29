package tech.hiddenproject.progressive.exception;

import tech.hiddenproject.progressive.Game;

/**
 * Thrown from {@link Game}.
 */
public class GameException extends RuntimeException {

  public GameException(String message) {
    super(message);
  }
}
