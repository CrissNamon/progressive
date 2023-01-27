package tech.hiddenproject.progressive.basic.injection;

import java.util.Arrays;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.injection.PackageLoader;

/**
 * Simple implementation of {@link PackageLoader}.
 */
public class SimplePackageLoader implements PackageLoader {

  public static final String[] FORBIDDEN_PACKAGE_PREFIX = {
      "tech.hiddenproject.progressive",
      "java.",
      "sun.",
      "javax",
      "com.intellij.rt",
      "org.xml.sax",
      "org.w3c.dom",
      "jdk.internal",
      "org.codehaus.classworlds",
      "org.codehaus.plexus",
      "org.slf4j",
      "org.apache.log4j"
  };

  /**
   * Gets all available packages using {@link Package#getPackages()}.
   *
   * @return Array of available packages
   */
  @Override
  public Package[] loadAllPackages() {
    return Arrays.stream(Package.getPackages())
        .filter(this::isPackageForbidden)
        .collect(Collectors.toList())
        .toArray(new Package[]{});
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

  private boolean isPackageForbidden(Package pack) {
    return Arrays.stream(FORBIDDEN_PACKAGE_PREFIX)
        .noneMatch(prefix -> pack.getName().startsWith(prefix));
  }
}
