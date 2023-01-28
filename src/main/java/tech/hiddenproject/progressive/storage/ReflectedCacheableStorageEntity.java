package tech.hiddenproject.progressive.storage;

/**
 * @author Danila Rassokhin
 */
public interface ReflectedCacheableStorageEntity<I> extends ReflectedStorageEntity<I> {

  void saveMetadata(EntityMetadata entityMetadata);

  @Override
  default void init() {
    saveMetadata(createMetaData());
  }
}
