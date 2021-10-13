package ru.danilarassokhin.progressive.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicGameTest {

    @Test
    public void addGameObjectNoClassSpecifiedTest() {
        Assertions.assertThrows(RuntimeException.class, () -> BasicGame.getInstance().addGameObject());
    }

    @Test
    public void setFrameTimeLessThanOne() {
        Assertions.assertThrows(RuntimeException.class, () -> BasicGame.getInstance().setFrameTime(-1));
    }

}
