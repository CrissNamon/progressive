package tech.hiddenproject.progressive.injection.variant;

import tech.hiddenproject.progressive.annotation.GameBean;

@GameBean(variant = "Android")
public class AndroidVariant implements ItemVariant {

  @Override
  public String getVariant() {
    return "Android";
  }
}
