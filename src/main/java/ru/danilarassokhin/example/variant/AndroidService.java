package ru.danilarassokhin.example.variant;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

/**
 * This service will be loaded to BasicDIContainer if
 * android variant was specified in BasicDIContainer
 */
@GameBean(variant = "android")
public class AndroidService implements MyService {
  @Override
  public void printVariant() {
    BasicComponentManager
        .getGameLogger().info("I'm Android service");
  }
}
