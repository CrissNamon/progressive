package progressive.data;

import java.util.Objects;
import java.util.Random;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.injection.GameBeanCreationPolicy;

/**
 * @author Danila Rassokhin
 */
@GameBean(policy = GameBeanCreationPolicy.OBJECT)
public class ObjectBean {

  private final int id;

  public ObjectBean() {
    this.id = new Random().nextInt();
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
    ObjectBean that = (ObjectBean) o;
    return getId() == that.getId();
  }
}
