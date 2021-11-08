package ru.danilarassokhin.progressive.injection;

public interface PackageLoader {

  /**
   * Gets all available packages.
   *
   * @return Array of available packages
   */
  Package[] loadAllPackages();

  /**
   * Gets {@link java.lang.Package} by it's name.
   *
   * @param packageName Package name to get
   * @return {@link java.lang.Package} of given {@code packageName} or null
   */
  Package forName(String packageName);

}
