package tech.hiddenproject.example.proxy;

import tech.hiddenproject.example.game.component.GameItem;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.Autofill;
import tech.hiddenproject.progressive.annotation.Intercept;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.annotation.Qualifier;

@Proxy(LoggerMethodInterceptor.class)
public class TestProxyBean {

  @Autofill
  public TestProxyBean(@Qualifier("simpleGameItem") GameItem gameItem) {
    BasicComponentManager.getGameLogger().info("TEST PROXY CONSTRUCTOR: " + gameItem);
  }

  @Intercept
  public void print(String a) {
    BasicComponentManager.getGameLogger().info(a);
  }

  @Intercept
  public int getInt(int a) {
    BasicComponentManager.getGameLogger().info("GET INT = " + a);
    return a;
  }

  public void notIntercepted(String message) {
    BasicComponentManager.getGameLogger().info(message);
  }
}
