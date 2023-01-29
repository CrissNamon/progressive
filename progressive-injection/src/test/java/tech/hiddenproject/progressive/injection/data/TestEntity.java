package tech.hiddenproject.progressive.injection.data;

import java.util.Objects;
import tech.hiddenproject.progressive.annotation.Embedded;
import tech.hiddenproject.progressive.annotation.Nullable;
import tech.hiddenproject.progressive.storage.ReflectedStorageEntity;

/**
 * @author Danila Rassokhin
 */
public class TestEntity implements ReflectedStorageEntity<Long> {

  private final Long id;

  private final String title;

  @Embedded
  private final EmbeddedField embeddedField = new EmbeddedField();

  @Nullable
  private final String nullable = null;

  private final String notNullable = null;

  public TestEntity(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  @Override
  public Long getId() {
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
    TestEntity that = (TestEntity) o;
    return getId().equals(that.getId());
  }

  private static class EmbeddedField {

    private int name;

  }
}
