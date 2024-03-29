package tech.hiddenproject.progressive.injection;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * {@link BeanScanner} for {@link GameBean} classes.
 *
 * @author Danila Rassokhin
 */
public class GameBeanScanner implements BeanScanner {

  @Override
  public boolean shouldBeLoaded(Class<?> c) {
    return ComponentAnnotationProcessor.isAnnotationPresent(GameBean.class, c);
  }
}
