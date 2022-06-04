package tech.hiddenproject.example.proxy;

import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;

@Proxy(LoggerMethodInterceptor.class)
public class TestProxyBean {

  @Autofill
  public TestProxyBean(@Qualifier("simpleGameItem") GameItem gameItem) {
    BasicComponentManager.getGameLogger().info("TEST PROXY CONSTRUCTOR: " + gameItem);
  }

  @Intercept
  public void print(String a) {
    System.out.println(a);
  }

  @Intercept
  public int getInt(int a) {
    System.out.println("GET INT = " + a);
    return a;
  }

  public void notIntercepted(String message) {
    System.out.println(message);
  }
}
