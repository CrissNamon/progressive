package tech.hiddenproject.progressive.storage;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import tech.hiddenproject.progressive.basic.storage.BasicStorage;

/**
 * @author Danila Rassokhin
 */
public class DefaultStorageRepository implements StorageRepository {

  private final Class<? extends StorageEntity> entityClass;

  public DefaultStorageRepository(Class<? extends StorageRepository> repositoryClass) {
    this.entityClass = getEntityType(repositoryClass);
  }

  private static <T> Class<T> getEntityType(Class c) {
    Type typeOfRepository = Arrays.stream(c.getGenericInterfaces())
        .filter(inter -> inter.getTypeName().startsWith(StorageRepository.class.getTypeName()))
        .findFirst().orElseThrow(() -> new RuntimeException());
    return (Class<T>) ((ParameterizedType) typeOfRepository).getActualTypeArguments()[1];
  }

  @Override
  public void save(StorageEntity entity) {
    BasicStorage.INSTANCE.getTableFor(entityClass).put(entity);
  }

  @Override
  public Optional findById(Object id) {
    return Optional.ofNullable(BasicStorage.INSTANCE.getTableFor(entityClass).get(id));
  }

  @Override
  public Collection search(SearchCriteria searchCriteria) {
    return BasicStorage.INSTANCE.getTableFor(entityClass).search(searchCriteria);
  }
}
