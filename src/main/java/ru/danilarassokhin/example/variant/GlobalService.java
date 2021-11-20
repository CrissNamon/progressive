package ru.danilarassokhin.example.variant;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

//Implicit global variant is redundant
@GameBean(variant = GameBean.GLOBAL_VARIANT)
public class GlobalService {

  public void printVariant() {
    BasicComponentManager
        .getGameLogger().info("I'm GLOBAL service");
  }

}
