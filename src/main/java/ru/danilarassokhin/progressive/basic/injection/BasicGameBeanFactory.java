package ru.danilarassokhin.progressive.basic.injection;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.injection.GameBeanFactory;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

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
