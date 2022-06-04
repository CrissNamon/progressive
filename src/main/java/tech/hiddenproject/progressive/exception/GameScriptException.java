package tech.hiddenproject.progressive.exception;

import tech.hiddenproject.progressive.component.*;

/** Thrown from {@link GameScript}. */
public class GameScriptException extends RuntimeException {
  public GameScriptException(String message) {
    super(message);
  }
}
