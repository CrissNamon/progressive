package progressive.data;

import java.util.Objects;
import tech.hiddenproject.progressive.annotation.GameBean;

/**
 * @author Danila Rassokhin
 */
@GameBean
public class SingletonBean {

  private final int id;

  public SingletonBean() {
    this.id = 0;
  }

  public int getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SingletonBean that = (SingletonBean) o;
    return getId() == that.getId();
  }
}
