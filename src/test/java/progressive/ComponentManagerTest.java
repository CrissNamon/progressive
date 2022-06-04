package progressive;

import org.junit.jupiter.api.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.injection.*;

public class ComponentManagerTest {

  @Test
  public void testSingletonDIContainer() {
    DIContainer first = BasicComponentManager.getDiContainer();
    DIContainer second = BasicComponentManager.getDiContainer();

    Assertions.assertEquals(first, second);

    BasicComponentManager.setDiContainer(new BasicDIContainer(GameBean.DEFAULT_VARIANT));
    first = BasicComponentManager.getDiContainer();
    second = BasicComponentManager.getDiContainer();

    Assertions.assertEquals(first, second);
  }
}
