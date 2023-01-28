package tech.hiddenproject.progressive.storage;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Danila Rassokhin
 */
public interface StorageRepository<I, E extends StorageEntity<I>> {

  void save(E entity);

  Optional<E> findById(I id);

  Collection<E> search(SearchCriteria searchCriteria);

}
