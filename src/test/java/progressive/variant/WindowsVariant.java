package progressive.variant;

import tech.hiddenproject.progressive.annotation.*;

@GameBean(variant = "Windows")
public class WindowsVariant implements ItemVariant {
  @Override
  public String getVariant() {
    return "Windows";
  }
}
