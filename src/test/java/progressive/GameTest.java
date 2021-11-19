package progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

public class GameTest {
  @Test
  public void testStartAlreadyStarted() {
    Game game = BasicComponentManager.getGame();
    game.setStatic(true);

    Assertions.assertDoesNotThrow(game::start);
    Assertions.assertThrows(SecurityException.class, game::start);

    game.stop();
  }

  @Test
  public void testManualUpdateNotStatic() {
    Game game = BasicComponentManager.getGame();
    game.setStatic(false);
    game.setFrameTime(16);

    Assertions.assertDoesNotThrow(game::start);
    Assertions.assertThrows(SecurityException.class, () -> game.update(16));

    game.stop();
  }
}
