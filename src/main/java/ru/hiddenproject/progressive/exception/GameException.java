package ru.hiddenproject.progressive.exception;

/**
 * Thrown from {@link ru.hiddenproject.progressive.Game}.
 */
public class GameException extends RuntimeException {
  public GameException(String message) {
    super(message);
  }
}
