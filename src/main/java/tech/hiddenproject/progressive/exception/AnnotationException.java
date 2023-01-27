package tech.hiddenproject.progressive.exception;

/**
 * Thrown if exception connected with some annotation.
 */
public class AnnotationException extends RuntimeException {

  public AnnotationException(String message) {
    super(message);
  }
}
