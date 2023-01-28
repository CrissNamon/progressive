package tech.hiddenproject.progressive.basic.injection;

import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.exception.AnnotationException;
import tech.hiddenproject.progressive.exception.BeanUndefinedException;
import tech.hiddenproject.progressive.injection.BeanFactory;
import tech.hiddenproject.progressive.util.ClassProcessor;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Implementation of {@link BeanFactory} to process classes with {@link GameBean} annotation.
 */
public class GameBeanFactory implements BeanFactory {

  @Override
  public BeanDefinition createBeanMetaInformationFromClass(Class<?> beanClass) {
    if (beanClass.isInterface()) {
      throw new BeanUndefinedException("Can't create bean from interface!");
    }
    GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
    if (annotation == null) {
      throw new AnnotationException("No @GameBean annotation specified on " + beanClass);
    }
    String beanName = annotation.name();
    if (beanName.isEmpty()) {
      beanName = beanClass.getSimpleName().toLowerCase();
    }
    BeanDefinition beanDefinitionData = new BeanDefinition();
    beanDefinitionData.setName(beanName);
    beanDefinitionData.setVariant(annotation.variant());
    beanDefinitionData.setCreationPolicy(annotation.policy());
    beanDefinitionData.setRealType(beanClass);

    return beanDefinitionData;
  }

  @Override
  public boolean isShouldBeProcessed(Class<?> beanClass) {
    return !ClassProcessor.isPrimitive(beanClass) && !ClassProcessor.isCollection(beanClass)
        && ComponentAnnotationProcessor.isAnnotationPresent(GameBean.class, beanClass);
  }

  @Override
  public boolean isShouldBeCreated(Class<?> beanClass) {
    return isShouldBeProcessed(beanClass);
  }
}
