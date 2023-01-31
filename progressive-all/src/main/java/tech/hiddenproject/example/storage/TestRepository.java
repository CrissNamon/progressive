package tech.hiddenproject.example.storage;

import java.util.List;
import java.util.Optional;
import tech.hiddenproject.progressive.annotation.Repository;
import tech.hiddenproject.progressive.annotation.Select;
import tech.hiddenproject.progressive.storage.StorageRepository;

/**
 * @author Danila Rassokhin
 */
@Repository
public interface TestRepository extends StorageRepository<Long, TestEntity> {

  @Select("title = $0")
  Optional<TestEntity> findByByTitle(String title);

  @Select("embeddedField.embeddedId = $0")
  List<TestEntity> findByEmbeddedId(Integer id);

}
