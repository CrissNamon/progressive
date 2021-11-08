package ru.danilarassokhin.example;

import java.util.HashMap;
import ru.danilarassokhin.example.component.GameItem;
import ru.danilarassokhin.example.script.EchoSystem;
import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.GameFrameTimeType;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.GameInitializer;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.util.BasicComponentCreator;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameComponent;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.manager.GameState;

public class Main {

  public static void main(String[] args) throws BeanNotFoundException {
    //Initiate game components such as DI container and Game
    //autoScan = true means Scan all packages for beans and configurations classes
    GameInitializer.init(true);
    //Get DI container instance
    DIContainer diContainer = BasicComponentManager.getDiContainer();
    //State manager instance
    BasicGameStateManager stateManager = BasicGameStateManager.getInstance();
    //Subscribe to some instance. This will be executed when game will be initialized first time
    stateManager.<BasicGame>addListener(GameState.INIT, (g) -> BasicComponentManager
        .getGameLogger().info("GAME INITIATED\n"));

    //Get game instance
    Game game = BasicComponentManager.getGame();
    game.setGameObjectClass(BasicGameObject.class);
    game.setFrameTime(16);
    game.setStatic(false);
    game.setFrameTimeType(GameFrameTimeType.PARALLEL);

    for (int i = 0; i < 100; ++i) {
      //Add game object and attach EchoSystem
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
      game.addGameObject().getGameScript(EchoSystem.class);
    }

    //Get bean by it's name and class
    BasicObjectCaster basicObjectCaster = diContainer.getBean("objCaster", BasicObjectCaster.class);
    //There is only one bean of type BasicObjectCaster, so bean name is not required
    basicObjectCaster = diContainer.getBean(BasicObjectCaster.class);

    //Get bean as interface
    GameComponent simpleGameItem = diContainer.getBean("simpleGameItem", GameComponent.class);

    //Create proxy bean
    //if class annotated as @Proxy
    TestProxyBean proxyBean = BasicComponentCreator.create(TestProxyBean.class);
    //These methods will be intercepted, cause they are @Intercepted
    proxyBean.print("Hello");
    int a = proxyBean.getInt(1);
    assert a == 1;
    //This method won't be intercepted, cause it's not @Intercepted
    proxyBean.notIntercepted("Not intercepted");

    //if class is not annotated as @Proxy
    GameItem proxyBean1 = BasicComponentManager.getProxyCreator()
        .createProxy(GameItem.class, new LoggerMethodInterceptor());
    //All methods will be intercepted
    proxyBean1.setName("itemName");

    //Start game
    game.start();
    //Stop game
    game.stop();
    //Dispose all game objects
    game.dispose();
  }
}
