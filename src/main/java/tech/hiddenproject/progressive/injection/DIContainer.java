package tech.hiddenproject.progressive.injection;

import java.util.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.basic.injection.*;
import tech.hiddenproject.progressive.exception.*;

/** Represents Dependency Injection container. */
public interface DIContainer {

  /**
   * Initiates DI container with {@link SimplePackageLoader} and {@link SimplePackageScanner}. Will
   * be called after {@link GameInitializer#init(boolean)}.
   */
  void init();

  /**
   * Initiates DI container with {@code packageLoader} and {@code packageScanner}.
   *
   * @param packageLoader {@link SimplePackageLoader} to use
   * @param packageScanner {@link SimplePackageScanner} to use
   */
  void init(PackageLoader packageLoader, PackageScanner packageScanner);

  /**
   * Gets bean by it's name and class.
   *
   * @param name Bean name to find
   * @param beanClass Bean class to find
   * @param <V> Bean object to return
   * @return Bean object or throws BeanNotFoundException if bean not found
   */
  <V> V getBean(String name, Class<V> beanClass);

  /**
   * Gets random bean bean with given class.
   *
   * @param beanClass Bean class to find
   * @param <V> Bean object to return
   * @return Bean object or throws BeanNotFoundException if bean not found
   */
  <V> V getBean(Class<V> beanClass);

  /**
   * Searches for bean with given {@code beanClass}.
   *
   * @param beanClass Bean class to search
   * @param <V> Bean object to search
   * @return Optional bean
   */
  <V> Optional<V> searchBean(Class<V> beanClass);

  /**
   * Searches for bean with given {@code beanClass}.
   *
   * @param name Bean name to search
   * @param beanClass Bean class to search
   * @param <V> Bean object to search
   * @return Optional bean
   */
  <V> Optional<V> searchBean(String name, Class<V> beanClass);

  /**
   * Scans package {@code name} for {@link Configuration} and {@link GameBean} using {@code loader}.
   *
   * @param name Package name to scan
   * @param loader {@link PackageScanner} to use
   */
  void scanPackage(String name, PackageScanner loader);

  /**
   * Loads {@link GameBean} class into container.
   *
   * @param beanClass Class to load
   */
  void loadBean(Class<?> beanClass);

  /**
   * Loads configuration into container with custom {@link PackageScanner}.
   *
   * @param configClass Configuration class to load
   * @param scanner {@link PackageScanner} to use
   * @throws BeanUndefinedException Thrown on undefined exception
   */
  void loadConfiguration(Class<?> configClass, PackageScanner scanner)
      throws BeanUndefinedException;

  /**
   * Loads configuration into container.
   *
   * @param configClass Configuration class to load
   * @throws BeanUndefinedException Thrown on undefined exception
   */
  void loadConfiguration(Class<?> configClass) throws BeanUndefinedException;

  /**
   * Returns current variant defined in container.
   *
   * @return Current variant
   */
  String getVariant();
}
