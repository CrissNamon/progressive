package tech.hiddenproject.progressive.storage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Danila Rassokhin
 */
public enum BasicStorage implements Storage {

  INSTANCE;

  private final Map<Class<? extends StorageEntity>, EntityTable> dataBase = new ConcurrentHashMap<>();

  /**
   * Creates repository instance for given class.
   *
   * @param repositoryClass Repository class
   * @param <R>             Repository type
   * @return {@link StorageRepository}
   */
  public static <R extends StorageRepository> R createRepository(Class<R> repositoryClass) {
    return createRepository(repositoryClass, new RepositoryInterceptor(repositoryClass));
  }

  /**
   * Creates repository instance for given class.
   *
   * @param repositoryClass   Repository class
   * @param <R>               Repository type
   * @param invocationHandler Custom implementation of {@link InvocationHandler}
   * @return {@link StorageRepository}
   */
  public static <R extends StorageRepository> R createRepository(Class<R> repositoryClass,
                                                                 InvocationHandler invocationHandler) {
    return (R) Proxy.newProxyInstance(
        repositoryClass.getClassLoader(),
        new Class[]{repositoryClass},
        invocationHandler
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
