package ru.hiddenproject.example.variant;

import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.basic.BasicComponentManager;

/**
 * This service will be loaded to BasicDIContainer if
 * default variant was specified in BasicDIContainer
 * or wasn't specified at all
 */
@GameBean(variant = GameBean.DEFAULT_VARIANT)
public class WindowsService implements MyService {
  @Override
  public void printVariant() {
    BasicComponentManager
        .getGameLogger().info("I'm Windows service");
  }
}
