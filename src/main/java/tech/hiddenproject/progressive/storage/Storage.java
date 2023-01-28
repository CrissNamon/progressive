package tech.hiddenproject.progressive.storage;

/**
 * @author Danila Rassokhin
 */
public interface Storage {

  <I, E extends StorageEntity<I>> EntityTable<I, E> getTableFor(Class<E> entityClass);

}
