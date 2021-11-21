package ru.hiddenproject.example.game;

import java.util.ArrayList;
import java.util.List;
import ru.hiddenproject.example.game.script.CharacterSystem;
import ru.hiddenproject.progressive.basic.BasicGameObject;
import ru.hiddenproject.progressive.basic.BasicComponentManager;

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
