package tech.hiddenproject.example.storage;

import java.util.List;
import tech.hiddenproject.progressive.annotation.Select;
import tech.hiddenproject.progressive.storage.StorageRepository;

/**
 * @author Danila Rassokhin
 */
public interface TestRepository extends StorageRepository<Long, TestEntity> {

  @Select("title = $0")
  List<TestEntity> test(String title);

  @Select("embeddedField.embeddedId = $0")
  List<TestEntity> testEmbedded(Integer id);

}
