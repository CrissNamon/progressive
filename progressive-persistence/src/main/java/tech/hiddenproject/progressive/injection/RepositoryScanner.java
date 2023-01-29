package tech.hiddenproject.progressive.injection;

import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.Repository;

/**
 * {@link BeanScanner} for {@link Repository} interfaces.
 *
 * @author Danila Rassokhin
 */
public class RepositoryScanner implements BeanScanner {

  public RepositoryScanner() {
    if (BasicComponentManager.isDiContainerEnabled()) {
      BasicComponentManager.getDiContainer().addBeanScanner(this);
    }
  }

  @Override
  public boolean shouldBeLoaded(Class<?> c) {
    return c.isAnnotationPresent(Repository.class);
  }
}
