package tech.hiddenproject.progressive.storage;

/**
 * Implementation of {@link StorageEntity} which collects field values with reflection. Unlike
 * {@link ReflectedStorageEntity} {@link EntityMetadata} will be cached and field values will be
 * collected only once on {@link ReflectedStorageEntity#init()} call. Your entity class must
 * implement {@link ReflectedCacheableStorageEntity#saveMetadata(EntityMetadata)} to save
 * {@link EntityMetadata} internally.
 *
 * @param <I> Entity id type
 * @author Danila Rassokhin
 */
public interface ReflectedCacheableStorageEntity<I> extends ReflectedStorageEntity<I> {

  void saveMetadata(EntityMetadata entityMetadata);

  @Override
  default void init() {
    saveMetadata(createMetaData());
  }
}
