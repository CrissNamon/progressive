package ru.danilarassokhin.progressive.exception;

/**
 * Thrown if bean has some circular dependency.
 * <p>BeanA(BeanB) -> BeanB(BeanA)</p>
 */
public class BeanCircularDependencyException extends RuntimeException {

  private Class<?> dependency;

  public BeanCircularDependencyException(String message) {
    super(message);
  }

  public BeanCircularDependencyException(String message, Class<?> dependency) {
    super(message);
    this.dependency = dependency;
  }

  public Class<?> getDependency() {
    return dependency;
  }
}
