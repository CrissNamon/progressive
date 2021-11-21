package progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.BasicComponentManager;
import ru.hiddenproject.progressive.basic.BasicDIContainer;
import ru.hiddenproject.progressive.injection.DIContainer;

public class ComponentManagerTest {

  @Test
  public void testSingletonDIContainer() {
    DIContainer first = BasicComponentManager.getDiContainer();
    DIContainer second = BasicComponentManager.getDiContainer();

    Assertions.assertEquals(first, second);

    BasicComponentManager.setDiContainer(
        new BasicDIContainer(GameBean.DEFAULT_VARIANT)
    );
    first = BasicComponentManager.getDiContainer();
    second = BasicComponentManager.getDiContainer();

    Assertions.assertEquals(first, second);
  }

}
