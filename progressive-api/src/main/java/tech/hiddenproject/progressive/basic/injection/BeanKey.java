package tech.hiddenproject.progressive.basic.injection;

import java.util.Objects;

/**
 * Represents key for beans map in {@link tech.hiddenproject.progressive.injection.DIContainer}.
 */
public final class BeanKey {

  private final String name;
  private final Class<?> type;

  public BeanKey(String name, Class<?> type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    BeanKey beanKey = (BeanKey) obj;
    return this.name.equals(beanKey.getName()) && this.type == beanKey.getType();
  }

  @Override
  public String toString() {
    return name + ":" + type.getName();
  }

  public String getName() {
    return name;
  }

  public Class<?> getType() {
    return type;
  }
}
