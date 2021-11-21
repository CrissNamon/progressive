package ru.hiddenproject.example;

import ru.hiddenproject.example.game.GameExample;
import ru.hiddenproject.example.injection.DIContainerExample;
import ru.hiddenproject.example.proxy.ProxyExample;
import ru.hiddenproject.example.publisher.PublisherExample;
import ru.hiddenproject.example.variant.VariantsExample;
import ru.hiddenproject.progressive.basic.BasicComponentManager;
import ru.hiddenproject.progressive.exception.BeanNotFoundException;
import ru.hiddenproject.progressive.log.GameLogger;

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
