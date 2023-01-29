package tech.hiddenproject.progressive.injection.variant;

import tech.hiddenproject.progressive.annotation.GameBean;

@GameBean(variant = "Windows")
public class WindowsVariant implements ItemVariant {

  @Override
  public String getVariant() {
    return "Windows";
  }
}
