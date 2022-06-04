package progressive.variant;

import tech.hiddenproject.progressive.annotation.*;

@GameBean(variant = "Android")
public class AndroidVariant implements ItemVariant {
  @Override
  public String getVariant() {
    return "Android";
  }
}
