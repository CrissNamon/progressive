package tech.hiddenproject.progressive.storage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import tech.hiddenproject.progressive.annotation.Embedded;
import tech.hiddenproject.progressive.annotation.Nullable;
import tech.hiddenproject.progressive.util.ClassProcessor;

/**
 * Implementation of {@link StorageEntity} which collects field values with reflection. Unlike
 * {@link ReflectedCacheableStorageEntity} {@link EntityMetadata} won't be saved and field values
 * will be collected on every {@link ReflectedStorageEntity#getMetadata()} call. This implementation
 * can be slow, cause {@link EntityMetadata} won't be cached.
 *
 * @param <I> Entity id type
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
        .map(ClassProcessor::setAccessible)
        .filter(field -> includeField(field, from))
        .collect(HashMap::new, (map, field) -> collect(map, field, from, prefix), HashMap::putAll);
    return new EntityMetadata(metadata);
  }

  default Map<String, Object> collect(Map<String, Object> map, Field f, Object from,
                                      String prefix) {
    if (f.isAnnotationPresent(Embedded.class) && !ClassProcessor.isPrimitive(f)) {
      map.putAll(createMetaData(f.getType(), f.getName() + ".",
                                ClassProcessor.getFieldValue(f, from)
      ).getAll());
    } else {
      map.put(prefix + f.getName(), ClassProcessor.getFieldValue(f, from));
    }
    return map;
  }

  default boolean isNullable(Field f, Object from) {
    return ClassProcessor.getFieldValue(f, from) != null || f.isAnnotationPresent(Nullable.class);
  }

  default boolean isEmbedded(Field f) {
    return ClassProcessor.isPrimitive(f) || f.isAnnotationPresent(Embedded.class);
  }
}
