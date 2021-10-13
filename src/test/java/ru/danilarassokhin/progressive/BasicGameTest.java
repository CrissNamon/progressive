package ru.danilarassokhin.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.basic.BasicGameObject;

public class BasicGameTest {

    @Test
    public void setFrameTimeLessThanOne() {
        Assertions.assertThrows(RuntimeException.class, () -> BasicGame.getInstance().setFrameTime(-1));
    }

    @Test
    public void setGameObjectClassAlreadySet() {
        BasicGame.getInstance().setGameObjectClass(BasicGameObject.class);
        Assertions.assertFalse(() -> BasicGame.getInstance().setGameObjectClass(BasicGameObject.class));
    }

}
