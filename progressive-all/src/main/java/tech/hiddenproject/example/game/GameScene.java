package tech.hiddenproject.example.game;

import java.util.ArrayList;
import java.util.List;
import tech.hiddenproject.example.game.script.CharacterSystem;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.component.GameObject;

public class GameScene {

  private final List<GameObject> gameObjectList;

  public GameScene() {
    gameObjectList = new ArrayList<>();
  }

  public void loadScene() {
    GameObject mainCharacter = BasicComponentManager.getGame().addGameObject();
    gameObjectList.add(mainCharacter);
    CharacterSystem mainCharacterSystem = mainCharacter.getGameScript(CharacterSystem.class);
  }
}
