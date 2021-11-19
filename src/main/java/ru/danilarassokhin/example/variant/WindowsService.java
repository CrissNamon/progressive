package ru.danilarassokhin.example.variant;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;

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
