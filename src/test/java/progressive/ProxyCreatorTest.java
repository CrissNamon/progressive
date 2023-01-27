package progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import progressive.data.TestProxyClass;
import tech.hiddenproject.example.game.component.GameItem;
import tech.hiddenproject.example.proxy.LoggerMethodInterceptor;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.proxy.ProxyCreator;

public class ProxyCreatorTest {

  @Test
  public void testProxyClassNotEqualsOriginal() {
    ProxyCreator proxyCreator = BasicComponentManager.getProxyCreator();

    Assertions.assertNotEquals(
        GameItem.class,
        proxyCreator.createProxyClass(GameItem.class, new LoggerMethodInterceptor()));
  }

  @Test
  public void testProxyClassReturnMethodEqualsOriginal() {
    ProxyCreator proxyCreator = BasicComponentManager.getProxyCreator();

    TestProxyClass original = new TestProxyClass();
    TestProxyClass proxy =
        proxyCreator.createProxy(TestProxyClass.class, new LoggerMethodInterceptor());

    Assertions.assertEquals(original.getLong(1L), proxy.getLong(1L));
  }
}
