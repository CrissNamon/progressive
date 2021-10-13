package ru.danilarassokhin.progressive.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;

public class BasicGameObjectTest {

    @Test
    public void getGameScriptNoAnnotation() {
        BasicGame.getInstance().setGameObjectClass(BasicGameObject.class);
        GameObject testObject = BasicGame.getInstance().addGameObject();

        Assertions.assertNotNull(testObject);

        Assertions.assertThrows(RuntimeException.class, () -> testObject.getGameScript(GameScriptNoAnnotationTest.class));
    }


    @Test
    public void getGameScriptRequiredGameScriptNoAnnotation() {
        BasicGame.getInstance().setGameObjectClass(BasicGameObject.class);
        GameObject testObject = BasicGame.getInstance().addGameObject();

        Assertions.assertNotNull(testObject);

        Assertions.assertThrows(RuntimeException.class, () -> testObject.getGameScript(ValidGameScript.class));
    }

    @Test
    public void removeGameScriptWhichNotAttached() {
        BasicGame.getInstance().setGameObjectClass(BasicGameObject.class);
        GameObject testObject = BasicGame.getInstance().addGameObject();

        Assertions.assertNotNull(testObject);
        Assertions.assertFalse(() -> testObject.removeGameScript(ValidGameScript.class));
    }

    private class GameScriptNoAnnotationTest implements GameScript {

        @Override
        public GameObject gameObject() {
            return null;
        }

        @Override
        public void setGameObject(GameObject parent) {

        }
    }

    @RequiredGameScript(GameScriptNoAnnotationTest.class)
    private class ValidGameScript implements GameScript {

        @Override
        public GameObject gameObject() {
            return null;
        }

        @Override
        public void setGameObject(GameObject parent) {

        }
    }
}
