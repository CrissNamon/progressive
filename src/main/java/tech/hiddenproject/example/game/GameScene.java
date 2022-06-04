package tech.hiddenproject.example.game;

import java.util.*;
import tech.hiddenproject.example.game.script.*;
import tech.hiddenproject.progressive.basic.*;
import tech.hiddenproject.progressive.component.*;

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
