package tech.hiddenproject.example;

import tech.hiddenproject.example.game.GameExample;
import tech.hiddenproject.example.injection.DIContainerExample;
import tech.hiddenproject.example.proxy.ProxyExample;
import tech.hiddenproject.example.publisher.PublisherExample;
import tech.hiddenproject.example.statemachine.StateMachineExample;
import tech.hiddenproject.example.variant.VariantsExample;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.exception.BeanNotFoundException;
import tech.hiddenproject.progressive.log.GameLogger;

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

    gameLogger.info("STATE MACHINE EXAMPLE");
    StateMachineExample stateMachineExample = new StateMachineExample();
  }
}
