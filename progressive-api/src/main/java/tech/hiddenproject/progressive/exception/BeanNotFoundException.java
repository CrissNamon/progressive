package tech.hiddenproject.progressive.exception;

/**
 * Thrown if bean is not presented in DI container.
 */
public class BeanNotFoundException extends AbstractException {

  public BeanNotFoundException(Throwable cause) {
    super(cause);
  }

  public BeanNotFoundException(String message, Object... args) {
    super(message, args);
  }

  public BeanNotFoundException(String message, Throwable cause, Object... args) {
    super(message, cause, args);
  }

  public static BeanNotFoundException of(String message, Object... args) {
    return new BeanNotFoundException(message, args);
  }
}
