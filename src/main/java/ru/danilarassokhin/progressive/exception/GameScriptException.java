package ru.danilarassokhin.progressive.exception;

/**
 * Thrown from {@link ru.danilarassokhin.progressive.component.GameScript}.
 */
public class GameScriptException extends RuntimeException {
  public GameScriptException(String message) {
    super(message);
  }
}
