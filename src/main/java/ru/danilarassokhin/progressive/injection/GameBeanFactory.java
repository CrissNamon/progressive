package ru.danilarassokhin.progressive.injection;

import ru.danilarassokhin.progressive.basic.injection.Bean;

/**
 * Helps to process beans.
 */
public interface GameBeanFactory {

  /**
   * Creates meta information from class annotated with
   * {@link ru.danilarassokhin.progressive.annotation.GameBean}.
   *
   * @param beanClass Class to create information from
   * @return {@link ru.danilarassokhin.progressive.basic.injection.Bean}
   */
  Bean createBeanMetaInformationFromClass(Class<?> beanClass);

}
