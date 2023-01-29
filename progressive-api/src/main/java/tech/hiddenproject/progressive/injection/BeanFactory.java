package tech.hiddenproject.progressive.injection;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.injection.BeanDefinition;

/**
 * Helps to process beans.
 */
public interface BeanFactory {

  /**
   * Creates meta information from class annotated with {@link GameBean}.
   *
   * @param beanClass Class to create information from
   * @return {@link BeanDefinition}
   */
  BeanDefinition createBeanMetaInformationFromClass(Class<?> beanClass);

  /**
   * Checks if given class should be loaded into container as a bean.
   *
   * @param beanClass Class to check
   * @return true - if class is a bean
   */
  boolean isShouldBeProcessed(Class<?> beanClass);

  boolean isShouldBeCreated(Class<?> beanClass);
}
