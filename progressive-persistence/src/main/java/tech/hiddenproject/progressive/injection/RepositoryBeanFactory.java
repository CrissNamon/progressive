package tech.hiddenproject.progressive.injection;

import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.annotation.Repository;
import tech.hiddenproject.progressive.basic.injection.BeanDefinition;
import tech.hiddenproject.progressive.storage.BasicStorage;
import tech.hiddenproject.progressive.storage.StorageRepository;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * {@link BeanFactory} for {@link Repository} interfaces.
 *
 * @author Danila Rassokhin
 */
public class RepositoryBeanFactory implements BeanFactory {

  public RepositoryBeanFactory() {
    if (BasicComponentManager.isDiContainerEnabled()) {
      BasicComponentManager.getDiContainer().addBeanFactory(this);
    }
  }

  @Override
  public BeanDefinition createBeanMetaInformationFromClass(Class<?> beanClass) {
    Repository annotation = ComponentAnnotationProcessor.findAnnotation(
        beanClass, Repository.class);
    if (annotation == null) {
      throw new RuntimeException("No @Repository annotation specified!");
    }
    String beanName = annotation.value();
    if (beanName.isEmpty()) {
      beanName = beanClass.getSimpleName().toLowerCase();
    }
    BeanDefinition beanDefinitionData = new BeanDefinition();
    beanDefinitionData.setName(beanName);
    beanDefinitionData.setVariant(GameBean.GLOBAL_VARIANT);
    beanDefinitionData.setCreationPolicy(GameBeanCreationPolicy.SINGLETON);
    beanDefinitionData.setRealType(beanClass);
    beanDefinitionData.setBean(BasicStorage.createRepository(cast(beanClass)));
    beanDefinitionData.setCreated(true);
    beanDefinitionData.setReady(true);
    return beanDefinitionData;
  }

  @Override
  public boolean isShouldBeProcessed(Class<?> beanClass) {
    return ComponentAnnotationProcessor.isAnnotationPresent(Repository.class, beanClass)
        && isStorageRepository(beanClass);
  }

  @Override
  public boolean isShouldBeCreated(Class<?> beanClass) {
    return false;
  }

  private Class<? extends StorageRepository> cast(Class<?> beanClass) {
    return (Class<? extends StorageRepository>) beanClass;
  }

  private boolean isStorageRepository(Class<?> beanClass) {
    try {
      cast(beanClass);
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }
}
