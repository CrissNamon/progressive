package progressive;

import org.junit.jupiter.api.*;
import progressive.variant.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.basic.injection.*;
import tech.hiddenproject.progressive.exception.*;

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
}
