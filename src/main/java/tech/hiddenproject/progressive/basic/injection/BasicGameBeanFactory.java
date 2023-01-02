package tech.hiddenproject.progressive.basic.injection;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.injection.GameBeanFactory;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic implementation of {@link GameBeanFactory}.
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
