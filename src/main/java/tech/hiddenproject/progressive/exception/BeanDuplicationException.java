package tech.hiddenproject.progressive.exception;

import tech.hiddenproject.progressive.injection.*;

/** Thrown if bean with specified name and type already exist in {@link DIContainer}. */
public class BeanDuplicationException extends RuntimeException {
  public BeanDuplicationException(String message) {
    super(message);
  }
}
