package tech.hiddenproject.progressive.exception;

/**
 * Thrown if bean has some circular dependency.
 *
 * <p>BeanA(BeanB) -> BeanB(BeanA)
 */
public class BeanCircularDependencyException extends AbstractException {

  public BeanCircularDependencyException(Throwable cause) {
    super(cause);
  }

  public BeanCircularDependencyException(String message, Object... args) {
    super(message, args);
  }

  public BeanCircularDependencyException(String message, Throwable cause, Object... args) {
    super(message, cause, args);
  }

  public static BeanCircularDependencyException of(String message, Object... args) {
    return new BeanCircularDependencyException(message, args);
  }
}
