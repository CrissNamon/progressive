package ru.danilarassokhin.progressive.lambda;

/**
 * Represents some story condition
 */
public interface GameCondition {

  /**
   * Checks condition
   *
   * @return true if condition is completed
   */
  boolean isTrue();

}
