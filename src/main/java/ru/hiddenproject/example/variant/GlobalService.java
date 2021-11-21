package ru.hiddenproject.example.variant;

import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.BasicComponentManager;

//Implicit global variant is redundant
@GameBean(variant = GameBean.GLOBAL_VARIANT)
public class GlobalService {

  public void printVariant() {
    BasicComponentManager
        .getGameLogger().info("I'm GLOBAL service");
  }

}
