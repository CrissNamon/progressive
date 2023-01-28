package tech.hiddenproject.progressive.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents {@link StorageEntity} metadata as key-value pairs.
 *
 * @author Danila Rassokhin
 */
public class EntityMetadata {

  private final Map<String, Object> metadata;

  public EntityMetadata(Map<String, Object> metadata) {
    this.metadata = Collections.synchronizedMap(new HashMap<>());
    this.metadata.putAll(metadata);
  }

  public Map<String, Object> getAll() {
    return metadata;
  }

  public Object get(String key) {
    return metadata.get(key);
  }

  public void put(String key, String value) {
    metadata.put(key, value);
  }
}
