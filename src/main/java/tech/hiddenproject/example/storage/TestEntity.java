package tech.hiddenproject.example.storage;

import java.util.Objects;
import tech.hiddenproject.progressive.annotation.Embedded;
import tech.hiddenproject.progressive.storage.ReflectedStorageEntity;

/**
 * @author Danila Rassokhin
 */
public class TestEntity implements ReflectedStorageEntity<Long> {

  private final Long id;

  private final String title;

  private final String name = null;

  @Embedded
  private final EmbeddedField embeddedField = new EmbeddedField(1);

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

  @Override
  public String toString() {
    return "TestEntity{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }

  private static class EmbeddedField {

    private final int embeddedId;

    public EmbeddedField(int embeddedId) {
      this.embeddedId = embeddedId;
    }

    @Override
    public String toString() {
      return "EmbeddedField{" +
          "embeddedId=" + embeddedId +
          '}';
    }
  }
}
