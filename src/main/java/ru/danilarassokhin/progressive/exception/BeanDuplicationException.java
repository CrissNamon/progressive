package ru.danilarassokhin.progressive.exception;

/**
 * Thrown if bean with specified name and type already
 * exist in {@link ru.danilarassokhin.progressive.injection.DIContainer}.
 */
public class BeanDuplicationException extends RuntimeException {
  public BeanDuplicationException(String message) {
    super(message);
  }
}
