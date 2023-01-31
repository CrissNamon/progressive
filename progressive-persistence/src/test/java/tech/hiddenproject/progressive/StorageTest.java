package tech.hiddenproject.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.data.TestEntity;
import tech.hiddenproject.progressive.data.TestRepository;
import tech.hiddenproject.progressive.injection.BasicDIContainer;
import tech.hiddenproject.progressive.injection.BeanFactory;
import tech.hiddenproject.progressive.injection.BeanScanner;
import tech.hiddenproject.progressive.injection.RepositoryBeanFactory;
import tech.hiddenproject.progressive.injection.RepositoryScanner;
import tech.hiddenproject.progressive.injection.SimplePackageScanner;
import tech.hiddenproject.progressive.storage.BasicStorage;
import tech.hiddenproject.progressive.storage.EntityTable;
import tech.hiddenproject.progressive.storage.SearchCriteria;

/**
 * @author Danila Rassokhin
 */
public class StorageTest {

  @BeforeEach
  public void initEach() {
    BasicStorage.INSTANCE.clear();
  }

  @Test
  public void saveEntityTest() {
    TestEntity testEntity = new TestEntity(1L, "The peripheral");
    EntityTable<Long, TestEntity> entityTable = BasicStorage.INSTANCE.getTableFor(TestEntity.class);

    Assertions.assertDoesNotThrow(() -> entityTable.save(testEntity));
    Assertions.assertEquals(testEntity, entityTable.get(1L));
  }

  @Test
  public void searchEntityTest() {
    TestEntity testEntity = new TestEntity(2L, "The peripheral");
    EntityTable<Long, TestEntity> entityTable = BasicStorage.INSTANCE.getTableFor(TestEntity.class);
    entityTable.save(testEntity);

    SearchCriteria searchCriteria1 = SearchCriteria.createFromExpression("id = $0", 1L);
    SearchCriteria searchCriteria2 = SearchCriteria.createFromExpression("id = $0", 2L);

    Assertions.assertEquals(testEntity, entityTable.get(2L));
    Assertions.assertEquals(0, entityTable.search(searchCriteria1).size());
    Assertions.assertEquals(1, entityTable.search(searchCriteria2).size());
    Assertions.assertEquals(testEntity, entityTable.search(searchCriteria2).get(0));
  }

  @Test
  public void clearTest() {
    TestEntity testEntity = new TestEntity(1L, "The peripheral");
    EntityTable<Long, TestEntity> entityTable = BasicStorage.INSTANCE.getTableFor(TestEntity.class);

    entityTable.save(testEntity);

    Assertions.assertDoesNotThrow(entityTable::clear);
    Assertions.assertNull(entityTable.get(1L));

    entityTable.save(testEntity);

    Assertions.assertDoesNotThrow(BasicStorage.INSTANCE::clear);
    Assertions.assertNull(entityTable.get(1L));
  }

  @Test
  public void repositoryTest() {
    TestEntity testEntity = new TestEntity(1L, "The peripheral");
    TestRepository testRepository = BasicStorage.createRepository(TestRepository.class);
    SearchCriteria searchCriteria = SearchCriteria.createFromExpression("id = $0", 1L);

    Assertions.assertDoesNotThrow(() -> testRepository.save(testEntity));
    Assertions.assertEquals(testEntity, testRepository.findById(1L).get());
    Assertions.assertEquals(testEntity, testRepository.search(searchCriteria).get(0));
  }

  @Test
  public void testRepositoryBeanFactory() {
    BeanScanner beanScanner = new RepositoryScanner();
    BeanFactory beanFactory = new RepositoryBeanFactory();

    Assertions.assertTrue(beanScanner.shouldBeLoaded(TestRepository.class));
    Assertions.assertTrue(beanFactory.isShouldBeProcessed(TestRepository.class));
    Assertions.assertFalse(beanFactory.isShouldBeCreated(TestRepository.class));

    Assertions.assertFalse(beanScanner.shouldBeLoaded(TestEntity.class));
    Assertions.assertFalse(beanFactory.isShouldBeProcessed(TestEntity.class));
    Assertions.assertFalse(beanFactory.isShouldBeCreated(TestEntity.class));
  }

  @Test
  public void testRepositoryBean() {
    BasicDIContainer basicDIContainer = new BasicDIContainer();
    basicDIContainer.addBeanFactory(new RepositoryBeanFactory());
    basicDIContainer.addBeanScanner(new RepositoryScanner());
    basicDIContainer.loadBean(TestRepository.class);

    Assertions.assertDoesNotThrow(() -> basicDIContainer.getBean(TestRepository.class));
    Assertions.assertDoesNotThrow(() -> basicDIContainer.getBean(
        "TestRepository",
        TestRepository.class
    ));

    BasicDIContainer newContainer = new BasicDIContainer();
    newContainer.addBeanFactory(new RepositoryBeanFactory());
    newContainer.addBeanScanner(new RepositoryScanner());
    newContainer.scanPackage(
        "tech.hiddenproject.progressive.data", new SimplePackageScanner());

    Assertions.assertDoesNotThrow(() -> newContainer.getBean(TestRepository.class));
    Assertions.assertDoesNotThrow(() -> newContainer.getBean(
        "TestRepository",
        TestRepository.class
    ));
  }
}
