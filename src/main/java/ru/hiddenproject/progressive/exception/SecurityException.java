package ru.hiddenproject.progressive.exception;

import ru.hiddenproject.progressive.manager.GameSecurityManager;

/**
 * Thrown from {@link GameSecurityManager}.
 */
public class SecurityException extends RuntimeException {
  public SecurityException(String message) {
    super(message);
  }
}
