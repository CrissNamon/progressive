package tech.hiddenproject.example.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.basic.BasicDIContainer;
import tech.hiddenproject.progressive.exception.CriteriaException;
import tech.hiddenproject.progressive.storage.BasicStorage;
import tech.hiddenproject.progressive.storage.Criteria;
import tech.hiddenproject.progressive.storage.EntityTable;
import tech.hiddenproject.progressive.storage.SearchCriteria;

/**
 * @author Danila Rassokhin
 */
public class StorageExample {

  public StorageExample() {

    // Init DI container to create all repositories
    BasicDIContainer basicDIContainer = new BasicDIContainer();
    //basicDIContainer.init();
    basicDIContainer.loadBean(TestRepository.class);

    // Get repository from DI container if repository annotated as @Repository
    TestRepository testRepository = basicDIContainer.getBean(TestRepository.class);
    // Create repository using BasicStorage if repository is not annotated as @Repository
    testRepository = BasicStorage.createRepository(TestRepository.class);

    // Create entity instance
    TestEntity testEntity = new TestEntity(0L, "The peripheral");

    // Save entity
    testRepository.save(testEntity);

    // Find entity by id
    testRepository.findById(0L).orElseThrow(() -> new RuntimeException());

    // Use criteria for advanced search
    SearchCriteria searchCriteria =
        SearchCriteria.createFromExpression("title = $0", "The peripheral");

    // Use generic search() method which exists in any repository
    List<TestEntity> foundEntities = testRepository.search(searchCriteria);

    // Create shortcut methods with predefined criteria in repository using @Select
    // See TestRepository.class
    testRepository.findByByTitle("The peripheral").orElseThrow(() -> new RuntimeException());

    // You can search for embedded field values like rootFieldName.fieldName
    // Fields must be annotated as @Embedded to be included in search
    // See TestEntity.class and EmbeddedField.class
    searchCriteria = SearchCriteria.createFromExpression("embeddedField.embeddedId = $0", 1);
    foundEntities = testRepository.search(searchCriteria);

    // You can also do it in @Select from repository
    foundEntities = testRepository.findByEmbeddedId(1);

    // This query will throw exception, cause field name is null and is not annotated as @Nullable
    // Fields with null values won't be included in entity metadata if they are not annotated as @Nullable
    try {
      searchCriteria = SearchCriteria.createFromExpression("name = $0", "MyName");
      foundEntities = testRepository.search(searchCriteria);
    } catch (CriteriaException e) {
      BasicComponentManager.getGameLogger().info("");
    }

    // ADVANCED CRITERIA OPTIONS

    // Add new operation to criteria
    // You need to pass operation code (like >, <=, etc)
    // and a function which will accept entity field value and query value from criteria
    // It must return boolean as a result of operation
    // See SearchCriteria.addExternalOperation javadoc for more information
    SearchCriteria.addExternalOperation("^", this::checkClassOperation);
    Map<String, Object> testData = new HashMap<>();
    testData.put("name", "Hello");
    Criteria classTypeCriteria = SearchCriteria.createFromExpression("name ^ $0", String.class);
    BasicComponentManager.getGameLogger().info(classTypeCriteria.test(testData));

    // ADVANCED STORAGE, STORAGE TABLE, ENTITY USAGE

    // Instead of high-level api (repositories) you can use low-level access to storage

    // Get entity table from storage
    EntityTable<Long, TestEntity> testEntityTable = BasicStorage.INSTANCE.getTableFor(
        TestEntity.class);

    // Save entity in table
    testEntityTable.save(testEntity);

    // Get entity by id
    testEntityTable.get(1L);

    // Use criteria
    try {
      testEntityTable.search(searchCriteria);
    } catch (CriteriaException e) {
      BasicComponentManager.getGameLogger().info("");
    }

    // Get entity metadata
    testEntity.getMetadata();
  }

  public boolean checkClassOperation(Object arg, Object query) {
    return arg.getClass().equals(query);
  }
}
