package ru.danilarassokhin.progressive.exception;

/**
 * Thrown from {@link ru.danilarassokhin.progressive.Game}.
 */
public class GameException extends RuntimeException {
  public GameException(String message) {
    super(message);
  }
}
