package ru.danilarassokhin.example;

import java.util.ArrayList;
import java.util.List;
import ru.danilarassokhin.example.script.CharacterSystem;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

public class GameScene {

  private final List<BasicGameObject> gameObjectList;

  public GameScene() {
    gameObjectList = new ArrayList<>();
  }

  public void loadScene() {
    BasicGameObject mainCharacter = BasicComponentManager.getGame()
        .addGameObject();
    gameObjectList.add(mainCharacter);
    CharacterSystem mainCharacterSystem = mainCharacter.getGameScript(CharacterSystem.class);
  }

}
