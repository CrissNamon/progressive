package tech.hiddenproject.progressive.storage;

import java.util.List;
import java.util.function.Predicate;
import tech.hiddenproject.progressive.basic.storage.SearchCriteria.SearchCriteriaBuilder;

/**
 * Represents builder for {@link Criteria}.
 *
 * @author Danila Rassokhin
 */
public interface CriteriaBuilder {

  <T> SearchCriteriaBuilder or(Predicate<T> criteria, String arg);

  SearchCriteriaBuilder and();

  Criteria build();

  List<CriteriaPredicate> getCriterias();

  Criteria createFromExpression(String exp, Object... args);

}
