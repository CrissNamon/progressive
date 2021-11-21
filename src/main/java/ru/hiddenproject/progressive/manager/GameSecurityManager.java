package ru.hiddenproject.progressive.manager;

import java.util.Arrays;
import ru.hiddenproject.progressive.lambda.GameCondition;

/**
 * Helps securing any method with simple rules
 */
public final class GameSecurityManager {

  /**
   * Allows access to given classes, throws RuntimeException otherwise
   *
   * @param deniedMessage Message to include in exception
   * @param classes       Classes which are allowed to call this method
   * @throws SecurityException if caller class is not allowed to call protected method
   */
  public static void allowAccessTo(String deniedMessage, Class<?>... classes) {
    if (!Arrays.asList(classes).contains(getCallerClass())) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Denies access to given classes
   *
   * @param deniedMessage Message to include in exception
   * @param classes       Classes which are denied from calling this method
   * @throws SecurityException if caller class is not allowed to call protected method
   */
  public static void denyAccessTo(String deniedMessage, Class<?>... classes) {
    if (Arrays.asList(classes).contains(getCallerClass())) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Allows access if condition is met, throws RuntimeException otherwise
   *
   * @param deniedMessage Message to include in exception
   * @param processor     Condition to protect method with
   * @throws SecurityException if condition returns false
   */
  public static void allowAccessIf(String deniedMessage, GameCondition processor) {
    if (!processor.isTrue()) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Denies access if condition is met
   *
   * @param deniedMessage Message to include in exception
   * @param processor     Condition to protect method with
   * @throws SecurityException if condition returns true
   */
  public static void denyAccessIf(String deniedMessage, GameCondition processor) {
    if (processor.isTrue()) {
      throw new SecurityException(deniedMessage);
    }
  }

  /**
   * Returns a class which called method with `getCallerClass` called in
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