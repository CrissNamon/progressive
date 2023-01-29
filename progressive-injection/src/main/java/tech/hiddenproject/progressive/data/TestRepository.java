package tech.hiddenproject.progressive.data;

import tech.hiddenproject.progressive.annotation.Repository;
import tech.hiddenproject.progressive.storage.StorageRepository;

/**
 * @author Danila Rassokhin
 */
@Repository("TestRepository")
public interface TestRepository extends StorageRepository<Long, TestEntity> {

}
