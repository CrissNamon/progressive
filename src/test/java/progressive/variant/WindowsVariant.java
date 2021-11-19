package progressive.variant;

import ru.danilarassokhin.progressive.annotation.GameBean;

@GameBean(variant = "Windows")
public class WindowsVariant implements ItemVariant {
  @Override
  public String getVariant() {
    return "Windows";
  }
}
