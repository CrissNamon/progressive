package progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import progressive.data.ObjectBean;
import progressive.data.SingletonBean;
import progressive.variant.AndroidVariant;
import progressive.variant.GlobalVariant;
import progressive.variant.ItemVariant;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.basic.BasicDIContainer;
import tech.hiddenproject.progressive.basic.injection.SimplePackageScanner;
import tech.hiddenproject.progressive.exception.AnnotationException;
import tech.hiddenproject.progressive.exception.BeanDuplicationException;
import tech.hiddenproject.progressive.exception.BeanNotFoundException;

public class DIContainerTest {

  @Test
  public void testValidVariants() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("Windows");
    basicDIContainer.scanPackage("progressive.variant", new SimplePackageScanner());
    ItemVariant variantTestItem = basicDIContainer.getBean(ItemVariant.class);
    Assertions.assertEquals(basicDIContainer.getVariant(), variantTestItem.getVariant());
  }

  @Test
  public void testInvalidVariants() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("MacOs");
    basicDIContainer.scanPackage("progressive.variant", new SimplePackageScanner());
    Assertions.assertThrows(
        BeanNotFoundException.class, () -> basicDIContainer.getBean(ItemVariant.class));
  }

  @Test
  public void testGlobalVariant() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("SomeVariant");
    basicDIContainer.scanPackage("progressive.variant", new SimplePackageScanner());
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
}
