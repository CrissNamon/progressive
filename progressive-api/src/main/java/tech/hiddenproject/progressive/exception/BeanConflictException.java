package tech.hiddenproject.progressive.exception;

/**
 * Thrown if more than one bean of given type has been found, but no bean name specified.
 */
public class BeanConflictException extends RuntimeException {

  public BeanConflictException(String message) {
    super(message);
  }
}
