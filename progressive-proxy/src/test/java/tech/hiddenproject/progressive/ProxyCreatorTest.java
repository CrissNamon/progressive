package tech.hiddenproject.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.data.GameItem;
import tech.hiddenproject.progressive.data.LoggerMethodInterceptor;
import tech.hiddenproject.progressive.data.TestProxyClass;
import tech.hiddenproject.progressive.proxy.BasicProxyCreator;
import tech.hiddenproject.progressive.proxy.ProxyCreator;

public class ProxyCreatorTest {

  @BeforeAll
  public static void init() {
    BasicComponentManager.setProxyCreator(BasicProxyCreator.getInstance());
  }

  @Test
  public void testProxyClassNotEqualsOriginal() {
    ProxyCreator proxyCreator = BasicComponentManager.getProxyCreator();

    Assertions.assertNotEquals(
        GameItem.class,
        proxyCreator.createProxyClass(GameItem.class, new LoggerMethodInterceptor())
    );
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
