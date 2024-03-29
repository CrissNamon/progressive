package tech.hiddenproject.progressive.injection;

/**
 * Helps to find and process packages.
 */
public interface PackageLoader {

  /**
   * Gets all available packages.
   *
   * @return Array of available packages
   */
  Package[] loadAllPackages();

  /**
   * Gets {@link Package} by it's name.
   *
   * @param packageName Package name to get
   * @return {@link Package} of given {@code packageName} or null
   */
  Package forName(String packageName);
}
