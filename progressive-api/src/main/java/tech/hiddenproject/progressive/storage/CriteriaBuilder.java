package tech.hiddenproject.progressive.storage;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents builder for {@link Criteria}.
 *
 * @author Danila Rassokhin
 */
public interface CriteriaBuilder {

  <T> CriteriaBuilder or(Predicate<T> criteria, String arg);

  CriteriaBuilder and();

  Criteria build();

  List<CriteriaPredicate> getCriterias();

  Criteria createFromExpression(String exp, Object... args);

}
