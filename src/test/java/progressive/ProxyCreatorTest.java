package progressive;

import org.junit.jupiter.api.*;
import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.example.proxy.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.proxy.*;

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
