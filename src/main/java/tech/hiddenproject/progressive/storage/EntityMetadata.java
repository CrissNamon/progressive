package tech.hiddenproject.progressive.storage;

import java.util.Collections;
import java.util.Map;

/**
 * @author Danila Rassokhin
 */
public class EntityMetadata {

  private final Map<String, Object> metadata;

  public EntityMetadata(Map<String, Object> metadata) {
    this.metadata = Collections.unmodifiableMap(metadata);
  }

  public Map<String, Object> getAll() {
    return metadata;
  }

  public Object get(String key) {
    return metadata.get(key);
  }

  @Override
  public String toString() {
    return "EntityMetadata{" +
        "metadata=" + metadata +
        '}';
  }
}
