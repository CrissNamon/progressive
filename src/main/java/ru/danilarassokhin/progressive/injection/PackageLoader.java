package ru.danilarassokhin.progressive.injection;

import java.util.Set;

/**
 * Package loader created to help DI container with class loading
 * <p>If you have problems with loading classes from packages, create new package loader implementation (or use lambdas)
 * to use your own classloading algorithm in DI container while loading configurations with @ComponentScan annotation.
 * Or use @Components to directly point on your @GameBean classes to DI container
 * </p>
 */
public interface PackageLoader {
  Set<Class> findAllClassesIn(String packageName);
}
