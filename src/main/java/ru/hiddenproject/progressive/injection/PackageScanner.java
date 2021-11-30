package ru.hiddenproject.progressive.injection;

import java.util.Set;

/**
 * Package scanner created to help DI container with class loading.
 * <p>If you have problems with loading classes from packages,
 * create new package loader implementation (or use lambdas)
 * to use your own classloading algorithm in DI container
 * while loading configurations with @ComponentScan annotation.
 * Or use @Components to directly point on your @GameBean classes to DI container
 * </p>
 */
public interface PackageScanner {

  /**
   * Returns all available classes from package.
   *
   * @param packageName Package name to scan
   * @return Set of classes from given package
   */
  Set<Class<?>> findAllClassesIn(String packageName);

  default ClassLoader getDefaultClassLoader(Class<?> of) {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = of.getClassLoader();
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ex) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
      }
    }
    return cl;
  }
}
