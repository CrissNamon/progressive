package tech.hiddenproject.example;

import tech.hiddenproject.example.game.*;
import tech.hiddenproject.example.injection.*;
import tech.hiddenproject.example.proxy.*;
import tech.hiddenproject.example.publisher.*;
import tech.hiddenproject.example.variant.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.exception.*;
import tech.hiddenproject.progressive.log.*;

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
