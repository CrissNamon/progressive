package progressive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import progressive.data.TestScript;
import tech.hiddenproject.progressive.Game;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.basic.BasicGame;
import tech.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import tech.hiddenproject.progressive.component.GameObject;
import tech.hiddenproject.progressive.manager.GameEvent;

/**
 * @author Danila Rassokhin
 */
public class GameTest {

  private static final List<GameEvent> eventList = new ArrayList<>();

  @BeforeAll
  public static void init() {
    BasicGamePublisher.getInstance()
        .<GameEvent> subscribeOn(Game.GAME_PUBLISHER_TOPIC, eventList::add);
  }

  @BeforeEach
  public void initEach() {
    eventList.clear();
    BasicComponentManager.setGame(new BasicGame());
  }

  @Test
  public void gameEventsTest() {
    Game game = BasicComponentManager.getGame();
    game.setStatic(true);
    GameObject gameObject = game.addGameObject();
    game.start();
    game.update(0);
    game.stop();
    game.removeGameObject(gameObject);
    game.dispose();

    List<GameEvent> expected = new ArrayList<>();
    Collections.addAll(expected, GameEvent.INITIALIZATION, GameEvent.NEW_OBJECT, GameEvent.START,
                       GameEvent.PLAY, GameEvent.STOP, GameEvent.REMOVE_OBJECT,
                       GameEvent.GLOBAL_DISPOSE
    );

    Assertions.assertIterableEquals(expected, eventList);

    Assertions.assertDoesNotThrow(() -> (GameObject) eventList.get(1).getPayload());
    Assertions.assertEquals(1, ((GameObject) eventList.get(1).getPayload()).getId());

    Assertions.assertDoesNotThrow(() -> (Long) eventList.get(5).getPayload());
    Assertions.assertEquals(1, (Long) eventList.get(5).getPayload());
  }

  @Test
  public void gameScriptEventsTest() {
    Game game = BasicComponentManager.getGame();
    game.setStatic(true);
    GameObject gameObject = game.addGameObject();
    TestScript testScript = gameObject.getGameScript(TestScript.class);
    game.start();
    game.update(0);
    game.stop();
    game.removeGameObject(gameObject);
    game.dispose();

    Assertions.assertEquals(1, testScript.getStartCounter());
    Assertions.assertEquals(1, testScript.getUpdateCounter());
    Assertions.assertEquals(1, testScript.getStopCounter());
    Assertions.assertEquals(1, testScript.getDisposeCounter());
  }

  @Test
  public void updateDeltaTest() {
    Long expected = new Random().nextLong();
    Game game = BasicComponentManager.getGame();
    game.setStatic(true);
    GameObject gameObject = game.addGameObject();
    TestScript testScript = gameObject.getGameScript(TestScript.class);
    game.update(expected);

    Assertions.assertEquals(expected, testScript.getDelta());
  }

}
