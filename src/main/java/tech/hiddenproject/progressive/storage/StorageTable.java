package tech.hiddenproject.progressive.storage;

import java.util.List;

/**
 * @author Danila Rassokhin
 */
public interface StorageTable<I, E extends StorageEntity<I>> {

  void put(E entity);

  E get(I id);

  List<E> search(SearchCriteria searchCriteria);

}
