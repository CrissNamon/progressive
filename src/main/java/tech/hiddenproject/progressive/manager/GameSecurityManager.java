package tech.hiddenproject.progressive.manager;

import tech.hiddenproject.progressive.lambda.*;

/** Helps securing any method with simple rules. */
public final class GameSecurityManager {

  /**
   * Allows access if condition is met, throws RuntimeException otherwise.
   *
   * @param deniedMessage Message to include in exception
   * @param processor Condition to protect method with
   * @throws SecurityException if condition returns false
   */
  public static void allowAccessIf(String deniedMessage, GameCondition processor) {
    if (!processor.isTrue()) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Denies access if condition is met.
   *
   * @param deniedMessage Message to include in exception
   * @param processor Condition to protect method with
   * @throws SecurityException if condition returns true
   */
  public static void denyAccessIf(String deniedMessage, GameCondition processor) {
    if (processor.isTrue()) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Returns a class which called method with `getCallerClass` called in.
   *
   * @return Class or null on error
   */
  public static Class<?> getCallerClass() {
    try {
      String caller = new Exception().getStackTrace()[3].getClassName();
      return Class.forName(caller);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
