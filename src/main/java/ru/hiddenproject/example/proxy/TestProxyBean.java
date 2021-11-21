package ru.hiddenproject.example.proxy;

import ru.hiddenproject.example.game.component.GameItem;
import ru.hiddenproject.progressive.annotation.Autofill;
import ru.hiddenproject.progressive.annotation.Intercept;
import ru.hiddenproject.progressive.annotation.Proxy;
import ru.hiddenproject.progressive.annotation.Qualifier;
import ru.hiddenproject.progressive.basic.BasicComponentManager;

@Proxy(LoggerMethodInterceptor.class)
public class TestProxyBean {

  @Autofill
  public TestProxyBean(@Qualifier("simpleGameItem") GameItem gameItem) {
    BasicComponentManager.getGameLogger()
        .info("TEST PROXY CONSTRUCTOR: " + gameItem);
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
