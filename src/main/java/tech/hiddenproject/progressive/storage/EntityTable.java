package tech.hiddenproject.progressive.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Danila Rassokhin
 */
public class EntityTable<I, E extends StorageEntity<I>> implements StorageTable<I, E> {

  private final Map<I, E> table = new HashMap<>();

  public void put(E entity) {
    entity.init();
    table.put(entity.getId(), entity);
  }

  public E get(I id) {
    return table.get(id);
  }

  @Override
  public List<E> search(SearchCriteria searchCriteria) {
    return table.values().stream()
        .filter(entity -> searchCriteria.test(entity.getMetadata().getAll()))
        .collect(Collectors.toList());
  }
}
