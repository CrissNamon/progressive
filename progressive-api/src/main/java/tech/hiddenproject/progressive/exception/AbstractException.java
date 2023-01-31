package tech.hiddenproject.progressive.exception;

/**
 * @author Danila Rassokhin
 */
public class AbstractException extends RuntimeException {

  public AbstractException(Throwable cause) {
    super(cause);
  }

  public AbstractException(String message, Object... args) {
    super(String.format(message, args));
  }

  public AbstractException(String message, Throwable cause, Object... args) {
    super(String.format(message, args), cause);
  }
}
