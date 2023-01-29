package tech.hiddenproject.progressive.injection.data;

import tech.hiddenproject.progressive.annotation.Repository;
import tech.hiddenproject.progressive.storage.StorageRepository;

/**
 * @author Danila Rassokhin
 */
@Repository("TestRepository")
public interface TestRepository extends StorageRepository<Long, TestEntity> {

}
