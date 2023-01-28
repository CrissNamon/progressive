package tech.hiddenproject.progressive.storage;

import java.util.Map;

/**
 * Represents criteria to check if given key-value pairs matches some rules in criteria.
 *
 * @author Danila Rassokhin
 */
public interface Criteria {

  boolean test(Map<String, Object> args);

}
