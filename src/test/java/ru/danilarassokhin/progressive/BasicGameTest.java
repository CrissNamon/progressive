package ru.danilarassokhin.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.danilarassokhin.progressive.basic.BasicGame;

public class BasicGameTest {

    @Test
    public void setFrameTimeLessThanOne() {
        Assertions.assertThrows(RuntimeException.class, () -> BasicGame.getInstance().setFrameTime(-1));
    }

}
