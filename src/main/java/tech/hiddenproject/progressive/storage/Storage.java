package tech.hiddenproject.progressive.storage;

/**
 * Represents storage for entities.
 *
 * @author Danila Rassokhin
 */
public interface Storage {

  /**
   * Gets table for given entity class.
   *
   * @param entityClass Entity class, implementation of {@link StorageEntity}
   * @param <I>         Entity id type
   * @param <E>         Entity type
   * @return {@link StorageTable} which stores all entities of given class
   */
  <I, E extends StorageEntity<I>> EntityTable<I, E> getTableFor(Class<E> entityClass);

  /**
   * Calls {@link StorageTable#clear()} on all tables.
   */
  void clear();

}
