package ru.hiddenproject.progressive.basic.injection;

import ru.hiddenproject.progressive.injection.PackageLoader;

/**
 * Simple implementation of {@link ru.hiddenproject.progressive.injection.PackageLoader}.
 */
public class SimplePackageLoader implements PackageLoader {

  /**
   * Gets all available packages using {@link Package#getPackages()}.
   *
   * @return Array of available packages
   */
  @Override
  public Package[] loadAllPackages() {
    return Package.getPackages();
  }

  /**
   * Gets {@link java.lang.Package} by it's name using {@link Package#getPackage(String)}.
   *
   * @param packageName Package name to get
   * @return {@link java.lang.Package} of given {@code packageName} or null
   */
  @Override
  public Package forName(String packageName) {
    return Package.getPackage(packageName);
  }
}
