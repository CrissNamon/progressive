package tech.hiddenproject.progressive.basic.injection;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.injection.*;

/** Simple {@link PackageScanner} implementation. */
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
