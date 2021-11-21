package ru.hiddenproject.progressive.exception;

/**
 * Thrown if bean with specified name and type already
 * exist in {@link ru.hiddenproject.progressive.injection.DIContainer}.
 */
public class BeanDuplicationException extends RuntimeException {
  public BeanDuplicationException(String message) {
    super(message);
  }
}
