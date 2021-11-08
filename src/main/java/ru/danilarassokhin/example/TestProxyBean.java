package ru.danilarassokhin.example;

import ru.danilarassokhin.example.component.GameItem;
import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.annotation.Intercept;
import ru.danilarassokhin.progressive.annotation.Proxy;
import ru.danilarassokhin.progressive.annotation.Qualifier;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

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
