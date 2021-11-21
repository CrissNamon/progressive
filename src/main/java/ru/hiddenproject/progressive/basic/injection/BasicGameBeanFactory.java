package ru.hiddenproject.progressive.basic.injection;

import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.injection.GameBeanFactory;
import ru.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic implementation of {@link ru.hiddenproject.progressive.injection.GameBeanFactory}.
 */
public class BasicGameBeanFactory implements GameBeanFactory {

  @Override
  public Bean createBeanMetaInformationFromClass(Class<?> beanClass) {
    if (beanClass.isInterface()) {
      throw new RuntimeException("Can't create bean from interface!");
    }
    GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    if (annotation == null) {
      throw new RuntimeException("No @GameBean annotation specified!");
    }
    Bean beanData = new Bean();
    beanData.setCreationPolicy(annotation.policy());
    beanData.setRealType(beanClass);

    return beanData;
  }

}
