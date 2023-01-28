package tech.hiddenproject.progressive.storage;

import java.lang.reflect.Field;

/**
 * @author Danila Rassokhin
 */
public interface StorageEntity<I> {

  I getId();

  EntityMetadata getMetadata();

  Object getMetadataFrom(String field);

  void init();

  default boolean includeField(Field f, Object from) {
    return !f.getType().equals(EntityMetadata.class);
  }
}
