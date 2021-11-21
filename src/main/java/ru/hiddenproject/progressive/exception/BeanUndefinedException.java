package ru.hiddenproject.progressive.exception;

/**
 * Thrown if exception is undefined. Something just gone wrong, sadly...
 */
public class BeanUndefinedException extends RuntimeException {

  public final static String DEFAULT_MESSAGE = "Undefined error occurred";

  public BeanUndefinedException(String message) {
    super(message);
  }

  public BeanUndefinedException() {
    super(DEFAULT_MESSAGE);
  }

  public BeanUndefinedException(String message, Throwable cause) {
    super(message, cause);
  }

  public BeanUndefinedException(Throwable cause) {
    super(DEFAULT_MESSAGE, cause);
  }
}
