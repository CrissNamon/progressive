package tech.hiddenproject.progressive.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import tech.hiddenproject.progressive.ComponentCreator;
import tech.hiddenproject.progressive.exception.GameException;

/**
 * @author Danila Rassokhin
 */
public class SimpleComponentCreator implements ComponentCreator {

  private boolean isHandlesEnabled = true;

  @Override
  public <C> C create(Class<C> componentClass, Object... args) {
    try {
      Constructor<C> constructor = componentClass.getDeclaredConstructor(getArgsTypes(args));
      constructor.setAccessible(true);
      return constructor.newInstance(args);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
             InvocationTargetException e) {
      e.printStackTrace();
      throw new GameException(
          "Unable to create class: " + componentClass + "! Exception: " + e.getMessage());
    }
  }

  @Override
  public Object[] injectBeansToParameters(Class<?> beanClass, Class<?>[] parameterTypes,
                                          Annotation[][] parameterAnnotations) {
    return new Object[0];
  }

  @Override
  public void setIsHandlesEnabled(boolean isHandlesEnabled) {
    this.isHandlesEnabled = isHandlesEnabled;
  }

  @Override
  public boolean isHandlesEnabled() {
    return isHandlesEnabled;
  }
}
