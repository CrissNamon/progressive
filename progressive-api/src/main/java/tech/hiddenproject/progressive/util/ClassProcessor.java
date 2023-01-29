package tech.hiddenproject.progressive.util;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Helper to work with reflection on classes.
 *
 * @author Danila Rassokhin
 */
public class ClassProcessor {

  public static Object getFieldValue(Field f, Object from) {
    try {
      return f.get(from);
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  public static Field setAccessible(Field f) {
    f.setAccessible(true);
    return f;
  }

  public static boolean isCollection(Class type) {
    return Collection.class.isAssignableFrom(type);
  }

  public static boolean isPrimitive(Field f) {
    return f.getType().equals(String.class)
        || (f.getType().getSuperclass() != null && f.getType().getSuperclass().equals(Number.class))
        || f.getType().isPrimitive();
  }

  public static boolean isPrimitive(Class<?> c) {
    return c.equals(String.class)
        || (c.getSuperclass() != null && c.getSuperclass().equals(Number.class))
        || c.isPrimitive();
  }
}
