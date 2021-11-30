package ru.hiddenproject.example.variant;

import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.BasicComponentManager;

/**
 * This service will be loaded to BasicDIContainer if
 * android variant was specified in BasicDIContainer.
 */
@GameBean(variant = "android")
public class AndroidService implements MyService {
  @Override
  public void printVariant() {
    BasicComponentManager
        .getGameLogger().info("I'm Android service");
  }
}
