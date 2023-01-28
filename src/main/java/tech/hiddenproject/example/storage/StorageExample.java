package tech.hiddenproject.example.storage;

import java.util.List;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.basic.storage.BasicStorage;
import tech.hiddenproject.progressive.storage.EntityTable;
import tech.hiddenproject.progressive.storage.SearchCriteria;
import tech.hiddenproject.progressive.storage.Storage;

/**
 * @author Danila Rassokhin
 */
public class StorageExample {

  public StorageExample() {

    Storage storage = BasicStorage.INSTANCE;
    EntityTable<Long, TestEntity> testEntityTable = storage.getTableFor(TestEntity.class);

    TestEntity testEntity = new TestEntity(0L, "The peripheral");
    testEntityTable.put(testEntity);

    BasicComponentManager.getGameLogger().info("Entity: " + testEntity);
    BasicComponentManager.getGameLogger().info("Metadata: " + testEntity.getMetadata());

    SearchCriteria searchCriteria = SearchCriteria.createFromExpression(
        "title = $0 & embeddedField.embeddedId > $1", "The peripheral", 1);
    BasicComponentManager.getGameLogger().info("Search: " + testEntityTable.search(searchCriteria));

    TestRepository testRepository = BasicStorage.createRepository(TestRepository.class);
    List<TestEntity> searchWithRepository = testRepository.test("The peripheral");
    BasicComponentManager.getGameLogger().info(searchWithRepository);
    searchWithRepository = testRepository.testEmbedded(1);
    BasicComponentManager.getGameLogger().info(searchWithRepository);

    testRepository.save(testEntity);
    testRepository.findById(0L)
        .ifPresent(testEntity1 -> BasicComponentManager.getGameLogger().info("Found entity!"));
  }
}
