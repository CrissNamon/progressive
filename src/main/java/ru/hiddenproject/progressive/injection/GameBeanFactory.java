package ru.hiddenproject.progressive.injection;

import ru.hiddenproject.progressive.basic.injection.Bean;

/**
 * Helps to process beans.
 */
public interface GameBeanFactory {

  /**
   * Creates meta information from class annotated with
   * {@link ru.hiddenproject.progressive.annotation.GameBean}.
   *
   * @param beanClass Class to create information from
   * @return {@link ru.hiddenproject.progressive.basic.injection.Bean}
   */
  Bean createBeanMetaInformationFromClass(Class<?> beanClass);

}
