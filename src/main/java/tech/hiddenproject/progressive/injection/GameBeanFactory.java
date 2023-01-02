package tech.hiddenproject.progressive.injection;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.injection.Bean;

/**
 * Helps to process beans.
 */
public interface GameBeanFactory {

  /**
   * Creates meta information from class annotated with {@link GameBean}.
   *
   * @param beanClass Class to create information from
   * @return {@link Bean}
   */
  Bean createBeanMetaInformationFromClass(Class<?> beanClass);
}
