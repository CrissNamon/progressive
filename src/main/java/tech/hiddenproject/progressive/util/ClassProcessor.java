package tech.hiddenproject.progressive.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Danila Rassokhin
 */
public class ClassProcessor {

  public static Optional<Object> getFieldValue(String name, Object from) {
    try {
      Field field = from.getClass().getDeclaredField(name.trim());
      field.setAccessible(true);
      return Optional.ofNullable(field.get(from));
    } catch (IllegalAccessException | NoSuchFieldException e) {
      return Optional.empty();
    }
  }

  public static void setFieldValue(String name, Object value, Object to) {
    try {
      Field field = to.getClass().getDeclaredField(name);
      field.setAccessible(true);
      field.set(to, value);
    } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e) {
      throw new RuntimeException(e);
    }
  }

  public static Type getGenericType(Class<?> c, String name) {
    try {
      return c.getDeclaredField(name)
          .getGenericType();
    } catch (NoSuchFieldException e) {
      return null;
    }
  }

  public static boolean isCollection(Class type) {
    return Collection.class.isAssignableFrom(type);
  }
}
