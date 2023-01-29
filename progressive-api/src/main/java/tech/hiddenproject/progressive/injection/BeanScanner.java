package tech.hiddenproject.progressive.injection;

/**
 * Check if class should be loaded into container.
 *
 * @author Danila Rassokhin
 */
public interface BeanScanner {

  /**
   * Check if given class should be loaded into container.
   *
   * @param c Class to check
   * @return true - if class should be loaded into container
   */
  boolean shouldBeLoaded(Class<?> c);
}
