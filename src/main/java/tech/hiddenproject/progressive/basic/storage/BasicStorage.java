package tech.hiddenproject.progressive.basic.storage;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import tech.hiddenproject.progressive.storage.EntityTable;
import tech.hiddenproject.progressive.storage.Storage;
import tech.hiddenproject.progressive.storage.StorageEntity;
import tech.hiddenproject.progressive.storage.StorageRepository;

/**
 * @author Danila Rassokhin
 */
public enum BasicStorage implements Storage {

  INSTANCE;

  private final Map<Class<? extends StorageEntity>, EntityTable> dataBase = new HashMap<>();

  public static <R extends StorageRepository> R createRepository(Class<R> repositoryClass) {
    return (R) Proxy.newProxyInstance(
        repositoryClass.getClassLoader(),
        new Class[]{repositoryClass},
        new RepositoryInterceptor(repositoryClass)
    );
  }

  @Override
  public <I, E extends StorageEntity<I>> EntityTable<I, E> getTableFor(Class<E> entityClass) {
    if (!dataBase.containsKey(entityClass)) {
      EntityTable<I, E> entityTable = new EntityTable<>();
      dataBase.put(entityClass, entityTable);
    }
    return (EntityTable<I, E>) dataBase.get(entityClass);
  }

  @Override
  public void clear() {
    dataBase.values().forEach(EntityTable::clear);
  }
}
