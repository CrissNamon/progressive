package tech.hiddenproject.progressive.storage;

import java.util.function.Predicate;

/**
 * @author Danila Rassokhin
 */
public class CriteriaPredicate {

  private final Predicate predicate;

  private final String arg;

  public CriteriaPredicate(Predicate predicate, String arg) {
    this.predicate = predicate;
    this.arg = arg;
  }

  public Predicate predicate() {
    return predicate;
  }

  public String arg() {
    return arg;
  }
}
