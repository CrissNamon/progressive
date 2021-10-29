package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.annotation.Intercept;
import ru.danilarassokhin.progressive.annotation.Proxy;

@GameBean(name = "TestProxyBean")
@Proxy(LoggerMethodInterceptor.class)
public class TestProxyBean {

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
