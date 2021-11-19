package progressive.variant;

import ru.danilarassokhin.progressive.annotation.GameBean;

@GameBean(variant = "Android")
public class AndroidVariant implements ItemVariant {
  @Override
  public String getVariant() {
    return "Android";
  }
}
