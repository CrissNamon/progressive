package tech.hiddenproject.example.proxy;

import tech.hiddenproject.example.game.component.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.basic.util.*;

public class ProxyExample {

  public ProxyExample() {
    // Create proxy bean
    // if class annotated as @Proxy
    TestProxyBean proxyBean = BasicComponentCreator.create(TestProxyBean.class);
    // These methods will be intercepted, cause they are @Intercepted
    proxyBean.print("Hello");
    int a = proxyBean.getInt(1);
    assert a == 1;
    // This method won't be intercepted, cause it's not @Intercepted
    proxyBean.notIntercepted("Not intercepted");

    // if class is not annotated as @Proxy
    GameItem proxyBean1 =
        BasicComponentManager.getProxyCreator()
            .createProxy(GameItem.class, new LoggerMethodInterceptor());
    // All methods will be intercepted
    proxyBean1.setName("itemName");
  }
}
