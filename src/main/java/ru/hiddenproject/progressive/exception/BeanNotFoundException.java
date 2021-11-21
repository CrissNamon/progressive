package ru.hiddenproject.progressive.exception;

/**
 * Thrown if bean is not presented in DI container
 */
public class BeanNotFoundException extends RuntimeException {

  private Class<?> beanClass;
  private String beanName;

  public BeanNotFoundException(String message) {
    super(message);
  }

  public BeanNotFoundException(String message, Class<?> beanClass) {
    super(message);
    this.beanClass = beanClass;
  }

  public BeanNotFoundException(String message, Class<?> beanClass, String beanName) {
    super(message);
    this.beanClass = beanClass;
    this.beanName = beanName;
  }

  public Class<?> getBeanClass() {
    return beanClass;
  }

  public String getBeanName() {
    return beanName;
  }
}
