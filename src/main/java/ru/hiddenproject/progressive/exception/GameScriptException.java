package ru.hiddenproject.progressive.exception;

/**
 * Thrown from {@link ru.hiddenproject.progressive.component.GameScript}.
 */
public class GameScriptException extends RuntimeException {
  public GameScriptException(String message) {
    super(message);
  }
}
