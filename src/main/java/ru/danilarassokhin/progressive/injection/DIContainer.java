package ru.danilarassokhin.progressive.injection;

import java.util.Optional;
import ru.danilarassokhin.progressive.basic.GameInitializer;
import ru.danilarassokhin.progressive.exception.BeanUndefinedException;

/**
 * Represents Dependency Injection container
 */
public interface DIContainer {

  /**
   * Initiates DI container with {@link ru.danilarassokhin.progressive.basic.injection.SimplePackageLoader}
   * and {@link ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner}.
   * Will be called after {@link GameInitializer#init(boolean)}.
   */
  void init();

  /**
   * Initiates DI container with {@code packageLoader} and {@code packageScanner}.
   *
   * @param packageLoader {@link ru.danilarassokhin.progressive.basic.injection.SimplePackageLoader} to use
   * @param packageScanner {@link ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner} to use
   */
  void init(PackageLoader packageLoader, PackageScanner packageScanner);

  /**
   * Gets bean by it's name and class
   *
   * @param name      Bean name to find
   * @param beanClass Bean class to find
   * @param <V>       Bean object to return
   * @return Bean object or throws RuntimeException if bean not found
   */
  <V> V getBean(String name, Class<V> beanClass);

  /**
   * Gets random bean bean with given class
   *
   * @param beanClass Bean class to find
   * @param <V>       Bean object to return
   * @return Bean object or throws RuntimeException if bean not found
   */
  <V> V getBean(Class<V> beanClass);

  /**
   * Searches for bean with given {@code beanClass}.
   *
   * @param beanClass Bean class to search
   * @param <V>       Bean object to search
   * @return Optional bean
   */
  <V> Optional<V> searchBean(Class<V> beanClass);

  /**
   * Searches for bean with given {@code beanClass}.
   *
   * @param name      Bean name to search
   * @param beanClass Bean class to search
   * @param <V>       Bean object to search
   * @return Optional bean
   */
  <V> Optional<V> searchBean(String name, Class<V> beanClass);

  /**
   * Scans package {@code name} for {@link ru.danilarassokhin.progressive.annotation.Configuration}
   * and {@link ru.danilarassokhin.progressive.annotation.GameBean} using {@code loader}.
   *
   * @param name Package name to scan
   * @param loader {@link PackageScanner} to use
   */
  void scanPackage(String name, PackageScanner loader);

  void loadBean(Class<?> beanClass);

  void loadConfiguration(Class<?> configClass, PackageScanner scanner) throws BeanUndefinedException;

  void loadConfiguration(Class<?> configClass) throws BeanUndefinedException;



}
