package tech.hiddenproject.progressive;

import java.util.HashSet;
import java.util.Set;

/**
 * Initializes component classes.
 *
 * @author Danila Rassokhin
 */
public final class ComponentInitializer {

  public static final String BASIC_DI_CONTAINER_CLASS
      = "tech.hiddenproject.progressive.injection.BasicDIContainer";
  public static final String BASIC_COMPONENT_CREATOR_CLASS
      = "tech.hiddenproject.progressive.injection.BasicComponentCreator";
  public static final String BASIC_PROXY_CREATOR_CLASS
      = "tech.hiddenproject.progressive.proxy.BasicProxyCreator";

  private static final Set<String> COMPONENT_CLASSES = new HashSet<>();

  static {
    COMPONENT_CLASSES.add(BASIC_COMPONENT_CREATOR_CLASS);
    COMPONENT_CLASSES.add(BASIC_DI_CONTAINER_CLASS);
    COMPONENT_CLASSES.add(BASIC_PROXY_CREATOR_CLASS);
    COMPONENT_CLASSES.add("tech.hiddenproject.progressive.injection.RepositoryBeanFactory");
    COMPONENT_CLASSES.add("tech.hiddenproject.progressive.injection.RepositoryBeanScanner");
  }

  /**
   * Initiates classes. After initialization removes all early added classes.
   */
  public static void init() {
    COMPONENT_CLASSES.forEach(ComponentInitializer::initClass);
    COMPONENT_CLASSES.clear();
  }

  /**
   * Adds new class to initialization list.
   *
   * @param className Name of class
   */
  public static void add(String className) {
    COMPONENT_CLASSES.add(className);
  }

  /**
   * Adds new class to initialization list.
   *
   * @param c Class
   */
  public static void add(Class<?> c) {
    add(c.getName());
  }

  private static void initClass(String className) {
    try {
      Class.forName(className);
    } catch (ClassNotFoundException ignored) {

    }
  }
}
