package ru.danilarassokhin.progressive.exception;

import ru.danilarassokhin.progressive.manager.GameSecurityManager;

/**
 * Thrown from {@link GameSecurityManager}.
 */
public class SecurityException extends RuntimeException {
  public SecurityException(String message) {
    super(message);
  }
}
