package tech.hiddenproject.progressive.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.exception.CriteriaException;

/**
 * @author Danila Rassokhin
 */
public class EntityTable<I, E extends StorageEntity<I>> implements StorageTable<I, E> {

  private final Map<I, E> table = new HashMap<>();

  public void save(E entity) {
    entity.init();
    table.put(entity.getId(), entity);
  }

  public E get(I id) {
    return table.get(id);
  }

  @Override
  public List<E> search(Criteria searchCriteria) throws CriteriaException {
    return table.values().stream()
        .filter(e -> searchCriteria.test(e.getMetadata().getAll()))
        .collect(Collectors.toList());
  }

  @Override
  public void clear() {
    table.clear();
  }
}
