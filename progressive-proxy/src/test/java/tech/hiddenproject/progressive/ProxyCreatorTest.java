package tech.hiddenproject.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.data.DiProxyClass;
import tech.hiddenproject.progressive.data.GameItem;
import tech.hiddenproject.progressive.data.LoggerMethodInterceptor;
import tech.hiddenproject.progressive.data.TestConfiguration;
import tech.hiddenproject.progressive.data.TestProxyClass;
import tech.hiddenproject.progressive.injection.BasicDIContainer;
import tech.hiddenproject.progressive.injection.DIContainer;
import tech.hiddenproject.progressive.proxy.BasicProxyCreator;
import tech.hiddenproject.progressive.proxy.ProxyCreator;

public class ProxyCreatorTest {

  @BeforeAll
  public static void init() {
    BasicComponentManager.setProxyCreator(BasicProxyCreator.getInstance());
  }

  @BeforeEach
  public void initEach() {
    BasicComponentManager.setDiContainer(new BasicDIContainer());
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

  @Test
  public void testProxyClassInjection() {
    ProxyCreator proxyCreator = BasicComponentManager.getProxyCreator();
    DIContainer diContainer = BasicComponentManager.getDiContainer();
    diContainer.loadConfiguration(TestConfiguration.class);

    DiProxyClass original = new DiProxyClass(1L);
    Class<DiProxyClass> proxyClass = proxyCreator.createProxyClass(DiProxyClass.class);

    diContainer.loadBean(proxyClass);

    DiProxyClass proxy = diContainer.getBean(proxyClass);

    Assertions.assertEquals(1L, original.getId());
    Assertions.assertEquals(2L, proxy.getId());
  }
}
