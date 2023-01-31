package tech.hiddenproject.example.variant;

import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.annotation.GameBean;

// Implicit global variant is redundant
@GameBean(variant = GameBean.GLOBAL_VARIANT)
public class GlobalService {

  public void printVariant() {
    BasicComponentManager.getGameLogger().info("I'm GLOBAL service");
  }
}
