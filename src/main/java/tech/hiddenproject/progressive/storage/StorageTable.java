package tech.hiddenproject.progressive.storage;

import java.util.List;
import tech.hiddenproject.progressive.basic.storage.SearchCriteria;

/**
 * Represents table of {@link Storage}. Store instances of specified type.
 *
 * @param <I> Entity id type
 * @param <E> Entity type
 * @author Danila Rassokhin
 */
public interface StorageTable<I, E extends StorageEntity<I>> {

  /**
   * Saves given entity.
   *
   * @param entity Entity to save
   */
  void save(E entity);

  /**
   * Gets entity by id.
   *
   * @param id Id to search
   * @return {@link StorageEntity}
   */
  E get(I id);

  /**
   * Searches for entities in this table with given {@link SearchCriteria}.
   *
   * @param searchCriteria {@link SearchCriteria}
   * @return List of all found entities
   */
  List<E> search(Criteria searchCriteria);

  /**
   * Removes all entities.
   */
  void clear();

}
