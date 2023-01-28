package tech.hiddenproject.progressive.storage;

import java.lang.reflect.Field;

/**
 * Represents some entity which can be saved in {@link StorageTable} within {@link Storage}.
 *
 * @param <I> Entity id type
 * @author Danila Rassokhin
 */
public interface StorageEntity<I> {

  /**
   * Id must be unique.
   *
   * @return Id of this entity
   */
  I getId();

  /**
   * @return {@link EntityMetadata}
   */
  EntityMetadata getMetadata();

  /**
   * Gets value from entity's field.
   *
   * @param field Field name to get value of
   * @return Field's value
   */
  Object getMetadataFrom(String field);

  /**
   * Called before {@link StorageTable#save(StorageEntity)} to initialize entity.
   */
  void init();

  /**
   * Checks if entity's field must be included in {@link EntityMetadata}.
   *
   * @param f    Field to check
   * @param from Class instance to get value of field from
   * @return true - if given field must be included in {@link EntityMetadata}.
   */
  default boolean includeField(Field f, Object from) {
    return !f.getType().equals(EntityMetadata.class);
  }
}
