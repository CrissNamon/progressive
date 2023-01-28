package tech.hiddenproject.progressive.storage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import tech.hiddenproject.progressive.annotation.Embedded;
import tech.hiddenproject.progressive.annotation.Nullable;

/**
 * @author Danila Rassokhin
 */
public interface ReflectedStorageEntity<I> extends StorageEntity<I> {

  @Override
  default EntityMetadata getMetadata() {
    return createMetaData();
  }

  @Override
  default Object getMetadataFrom(String field) {
    return getMetadata().get(field);
  }

  @Override
  default void init() {

  }

  @Override
  default boolean includeField(Field f, Object from) {
    return StorageEntity.super.includeField(f, from) && isNullable(f, from) && isEmbedded(f);
  }

  default EntityMetadata createMetaData() {
    return createMetaData(this.getClass(), "", this);
  }

  default EntityMetadata createMetaData(Class<?> c, String prefix, Object from) {
    Map<String, Object> metadata = Arrays.stream(c.getDeclaredFields())
        .map(this::setAccessible)
        .filter(field -> includeField(field, from))
        .collect(HashMap::new, (map, field) -> collect(map, field, from, prefix), HashMap::putAll);
    return new EntityMetadata(metadata);
  }

  default Field setAccessible(Field f) {
    f.setAccessible(true);
    return f;
  }

  default Map<String, Object> collect(Map<String, Object> map, Field f, Object from,
                                      String prefix) {
    if (f.isAnnotationPresent(Embedded.class) && !isPrimitive(f)) {
      map.putAll(createMetaData(f.getType(), f.getName() + ".", getFieldValue(f, from)).getAll());
    } else {
      map.put(prefix + f.getName(), getFieldValue(f, from));
    }
    return map;
  }

  default Object getFieldValue(Field f, Object from) {
    try {
      return f.get(from);
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  default boolean isNullable(Field f, Object from) {
    return getFieldValue(f, from) != null || f.isAnnotationPresent(Nullable.class);
  }

  default boolean isEmbedded(Field f) {
    return isPrimitive(f) || f.isAnnotationPresent(Embedded.class);
  }

  default boolean isPrimitive(Field f) {
    return f.getType().equals(String.class)
        || (f.getType().getSuperclass() != null && f.getType().getSuperclass().equals(Number.class))
        || f.getType().isPrimitive();
  }
}
