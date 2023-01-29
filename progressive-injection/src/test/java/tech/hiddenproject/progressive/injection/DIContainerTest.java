package tech.hiddenproject.progressive.injection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.exception.AnnotationException;
import tech.hiddenproject.progressive.exception.BeanDuplicationException;
import tech.hiddenproject.progressive.exception.BeanNotFoundException;
import tech.hiddenproject.progressive.injection.data.ObjectBean;
import tech.hiddenproject.progressive.injection.data.SingletonBean;
import tech.hiddenproject.progressive.injection.data.TestRepository;
import tech.hiddenproject.progressive.injection.variant.AndroidVariant;
import tech.hiddenproject.progressive.injection.variant.GlobalVariant;
import tech.hiddenproject.progressive.injection.variant.ItemVariant;

public class DIContainerTest {

  @Test
  public void testValidVariants() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("Windows");
    basicDIContainer.scanPackage(
        "tech.hiddenproject.progressive.injection.variant", new SimplePackageScanner());
    ItemVariant variantTestItem = basicDIContainer.getBean(ItemVariant.class);
    Assertions.assertEquals(basicDIContainer.getVariant(), variantTestItem.getVariant());
  }

  @Test
  public void testInvalidVariants() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("MacOs");
    basicDIContainer.scanPackage(
        "tech.hiddenproject.progressive.injection.variant", new SimplePackageScanner());
    Assertions.assertThrows(
        BeanNotFoundException.class, () -> basicDIContainer.getBean(ItemVariant.class));
  }

  @Test
  public void testGlobalVariant() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("SomeVariant");
    basicDIContainer.scanPackage(
        "tech.hiddenproject.progressive.injection.variant", new SimplePackageScanner());
    Assertions.assertDoesNotThrow(() -> basicDIContainer.getBean(GlobalVariant.class));
  }

  @Test
  public void testBeanDuplication() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("Android");

    Assertions.assertDoesNotThrow(() -> basicDIContainer.loadBean(AndroidVariant.class));
    Assertions.assertThrows(
        BeanDuplicationException.class, () -> basicDIContainer.loadBean(AndroidVariant.class));
  }

  @Test
  public void testBeanNotFound() {
    BasicDIContainer basicDIContainer = new BasicDIContainer(GameBean.DEFAULT_VARIANT);

    Assertions.assertThrows(
        BeanNotFoundException.class, () -> basicDIContainer.getBean(AndroidVariant.class));
  }

  @Test
  public void testBeanNoAnnotation() {
    BasicDIContainer basicDIContainer = new BasicDIContainer(GameBean.DEFAULT_VARIANT);

    Assertions.assertThrows(
        AnnotationException.class, () -> basicDIContainer.loadBean(BasicDIContainer.class));
  }

  @Test
  public void testObjectBean() {
    BasicDIContainer basicDIContainer = new BasicDIContainer();
    basicDIContainer.loadBean(ObjectBean.class);

    ObjectBean bean1 = basicDIContainer.getBean(ObjectBean.class);
    ObjectBean bean2 = basicDIContainer.getBean(ObjectBean.class);

    Assertions.assertNotEquals(bean1.getId(), bean2.getId());
    Assertions.assertNotEquals(bean1, bean2);
  }

  @Test
  public void testSingletonBean() {
    BasicDIContainer basicDIContainer = new BasicDIContainer();
    basicDIContainer.loadBean(SingletonBean.class);

    SingletonBean bean1 = basicDIContainer.getBean(SingletonBean.class);
    SingletonBean bean2 = basicDIContainer.getBean(SingletonBean.class);

    Assertions.assertEquals(bean1.getId(), bean2.getId());
    Assertions.assertEquals(bean1, bean2);
  }

  @Test
  public void testRepositoryBean() {
    BasicDIContainer basicDIContainer = new BasicDIContainer();
    basicDIContainer.addBeanFactory(new RepositoryBeanFactory());
    basicDIContainer.addBeanScanner(new RepositoryScanner());
    basicDIContainer.loadBean(TestRepository.class);

    Assertions.assertDoesNotThrow(() -> basicDIContainer.getBean(TestRepository.class));
    Assertions.assertDoesNotThrow(() -> basicDIContainer.getBean(
        "TestRepository",
        TestRepository.class
    ));

    BasicDIContainer newContainer = new BasicDIContainer();
    newContainer.addBeanFactory(new RepositoryBeanFactory());
    newContainer.addBeanScanner(new RepositoryScanner());
    newContainer.scanPackage(
        "tech.hiddenproject.progressive.injection.data", new SimplePackageScanner());

    Assertions.assertDoesNotThrow(() -> newContainer.getBean(TestRepository.class));
    Assertions.assertDoesNotThrow(() -> newContainer.getBean(
        "TestRepository",
        TestRepository.class
    ));
  }

  @Test
  public void testGameBeanFactory() {
    BeanScanner beanScanner = new GameBeanScanner();
    BeanFactory beanFactory = new GameBeanFactory();

    Assertions.assertTrue(beanScanner.shouldBeLoaded(SingletonBean.class));
    Assertions.assertTrue(beanFactory.isShouldBeProcessed(SingletonBean.class));
    Assertions.assertTrue(beanFactory.isShouldBeCreated(SingletonBean.class));

    Assertions.assertFalse(beanScanner.shouldBeLoaded(TestRepository.class));
    Assertions.assertFalse(beanFactory.isShouldBeProcessed(TestRepository.class));
    Assertions.assertFalse(beanFactory.isShouldBeCreated(TestRepository.class));
  }
}
