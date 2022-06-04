package tech.hiddenproject.example.variant;

import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;

// Implicit global variant is redundant
@GameBean(variant = GameBean.GLOBAL_VARIANT)
public class GlobalService {

  public void printVariant() {
    BasicComponentManager.getGameLogger().info("I'm GLOBAL service");
  }
}
