package tech.hiddenproject.progressive.injection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.BasicComponentManager;

/**
 * Simple {@link PackageScanner} implementation.
 */
public final class SimplePackageScanner implements PackageScanner {

  @Override
  public Set<Class<?>> findAllClassesIn(String packageName) {
    return findAllClassesUsingClassLoader(packageName);
  }

  private Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
    try {
      ClassLoader classLoader = getDefaultClassLoader(getClass());
      InputStream stream = classLoader.getResourceAsStream(packageName.replaceAll("[.]", "/"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      BasicComponentManager.getGameLogger().info("Scanning package: " + packageName);
      return reader
          .lines()
          .filter(line -> line.endsWith(".class"))
          .map(line -> getClass(line, packageName))
          .collect(Collectors.toSet());
    } catch (NullPointerException e) {
      return new HashSet<>();
    }
  }

  private Class<?> getClass(String className, String packageName) {
    try {
      return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class " + className + " not found!");
    }
  }
}
