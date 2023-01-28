package tech.hiddenproject.progressive.basic.injection;

import tech.hiddenproject.progressive.annotation.Repository;
import tech.hiddenproject.progressive.injection.BeanScanner;

/**
 * {@link BeanScanner} for {@link Repository} interfaces.
 *
 * @author Danila Rassokhin
 */
public class RepositoryScanner implements BeanScanner {

  @Override
  public boolean shouldBeLoaded(Class<?> c) {
    return c.isAnnotationPresent(Repository.class);
  }
}
