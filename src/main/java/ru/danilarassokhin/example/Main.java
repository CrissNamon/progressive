package ru.danilarassokhin.example;

import ru.danilarassokhin.example.game.GameExample;
import ru.danilarassokhin.example.injection.DIContainerExample;
import ru.danilarassokhin.example.proxy.ProxyExample;
import ru.danilarassokhin.example.publisher.PublisherExample;
import ru.danilarassokhin.example.variant.VariantsExample;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;
import ru.danilarassokhin.progressive.log.GameLogger;

public class Main {

  public static void main(String[] args) throws BeanNotFoundException {
    GameLogger gameLogger = BasicComponentManager.getGameLogger();

    gameLogger.info("DI CONTAINER EXAMPLE");
    DIContainerExample diContainerExample = new DIContainerExample();

    gameLogger.info("GAME EXAMPLE");
    GameExample gameExample = new GameExample();

    gameLogger.info("BEAN VARIANTS EXAMPLE");
    VariantsExample variantsExample = new VariantsExample();

    gameLogger.info("PROXY EXAMPLE");
    ProxyExample proxyExample = new ProxyExample();

    gameLogger.info("PUBLISHER EXAMPLE");
    PublisherExample publisherExample = new PublisherExample();
  }
}
