package ru.danilarassokhin.example;

import ru.danilarassokhin.example.game.GameExample;
import ru.danilarassokhin.example.injection.DIContainerExample;
import ru.danilarassokhin.example.proxy.ProxyExample;
import ru.danilarassokhin.example.variant.VariantsExample;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;

public class Main {

  public static void main(String[] args) throws BeanNotFoundException {
    DIContainerExample diContainerExample = new DIContainerExample();
    GameExample gameExample = new GameExample();
    VariantsExample variantsExample = new VariantsExample();
    ProxyExample proxyExample = new ProxyExample();
  }
}
