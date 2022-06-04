package tech.hiddenproject.progressive.exception;

import tech.hiddenproject.progressive.manager.*;

/** Thrown from {@link GameSecurityManager}. */
public class SecurityException extends RuntimeException {
  public SecurityException(String message) {
    super(message);
  }
}
