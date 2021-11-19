package progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import progressive.variant.AndroidVariant;
import progressive.variant.ItemVariant;
import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.BasicDIContainer;
import ru.danilarassokhin.progressive.basic.injection.SimplePackageScanner;
import ru.danilarassokhin.progressive.exception.AnnotationException;
import ru.danilarassokhin.progressive.exception.BeanDuplicationException;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;

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
    Assertions.assertThrows(BeanNotFoundException.class, () -> basicDIContainer.getBean(ItemVariant.class));
  }

  @Test
  public void testBeanDuplication() {
    BasicDIContainer basicDIContainer = new BasicDIContainer("Android");

    Assertions.assertDoesNotThrow(() -> basicDIContainer.loadBean(AndroidVariant.class));
    Assertions.assertThrows(BeanDuplicationException.class, () -> basicDIContainer.loadBean(AndroidVariant.class));
  }

  @Test
  public void testBeanNotFound() {
    BasicDIContainer basicDIContainer = new BasicDIContainer(GameBean.DEFAULT_VARIANT);

    Assertions.assertThrows(BeanNotFoundException.class, () -> basicDIContainer.getBean(AndroidVariant.class));
  }

  @Test
  public void testBeanNoAnnotation() {
    BasicDIContainer basicDIContainer = new BasicDIContainer(GameBean.DEFAULT_VARIANT);

    Assertions.assertThrows(AnnotationException.class, () -> basicDIContainer.loadBean(BasicDIContainer.class));
  }
}
