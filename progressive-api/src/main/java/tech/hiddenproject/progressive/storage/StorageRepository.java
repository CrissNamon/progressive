package tech.hiddenproject.progressive.storage;

import java.util.List;
import java.util.Optional;
import tech.hiddenproject.progressive.exception.CriteriaException;

/**
 * Represents repository to work with some {@link StorageEntity}.
 *
 * @param <I> Entity id type
 * @param <E> Entity type
 * @author Danila Rassokhin
 */
public interface StorageRepository<I, E extends StorageEntity<I>> {

  /**
   * Saves entity in the table. See {@link StorageTable#save(StorageEntity)}.
   *
   * @param entity Entity to save
   */
  void save(E entity);

  /**
   * Finds entity by given id.
   *
   * @param id Id to search
   * @return {@link Optional} of found entity
   */
  Optional<E> findById(I id);

  /**
   * Searches for entities by given {@link SearchCriteria}.
   *
   * @param searchCriteria {@link SearchCriteria}
   * @return List of all found entities
   */
  List<E> search(Criteria searchCriteria) throws CriteriaException;

}
