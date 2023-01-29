package tech.hiddenproject.progressive.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tech.hiddenproject.progressive.exception.CriteriaException;

/**
 * Represents search query for {@link StorageTable#search(Criteria)}. Supports basic operations:
 * <br>
 * - between values: >, <, >=, <=, =, ?= (fuzzy search) - between predicates: or, and, &, |
 * <br>
 * Search query should look like:
 * {@code <fieldName> <operation> $<argumentPosition> [[predicateOperation] ...]}
 * <br>
 * Example of query: {@code age >= $0 and height < $1 | name ?= $2}
 *
 * @author Danila Rassokhin
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SearchCriteria implements Criteria {

  private static final Map<String, BiFunction<Object, Object, Boolean>> externalOperations
      = new HashMap<>();
  private final List<SearchCriteriaBuilder> andBlocks;

  public SearchCriteria() {
    this.andBlocks = new ArrayList<>();
  }

  public static SearchCriteriaBuilder create() {
    return new SearchCriteria().builder();
  }

  /**
   * Supports basic operations:
   * <br>
   * - between values: >, <, >=, <=, =, ?= (fuzzy search) - between predicates: or, and, &, |
   * <br>
   * Search query should look like:
   * {@code <fieldName> <operation> $<argumentPosition> [[predicateOperation] ...]}
   * <br>
   * Example of query: {@code age >= $0 and height < $1 | name ?= $2}
   *
   * @param exp  Query expression
   * @param args Arguments to replace in query. Must be specified in correct position according
   *             numeration in query.
   * @return {@link SearchCriteria}
   */
  public static SearchCriteria createFromExpression(String exp, Object... args) {
    return SearchCriteria.create().createFromExpression(exp, args);
  }

  /**
   * Adds new operation type to criteria. This operation can be used between two values. First
   * argument of function is value which will be passed to {@link Criteria#test(Map)}. Second
   * argument of function is value which will be passed to
   * {@link SearchCriteria#createFromExpression(String, Object...)}. In
   * {@link StorageTable#search(Criteria)} first argument is an entity field, and second argument is
   * value which will be passed {@link Criteria} constructor.
   * <br>
   * Example: key = ^, predicate = {@code (arg, query) -> arg.getClass().equals(query)} This
   * operation will check if argument (entity's field value) class is equals to query class.
   * <br>
   *
   * @param key       Operation symbol
   * @param predicate Operation predicate
   */
  public static void addExternalOperation(String key,
                                          BiFunction<Object, Object, Boolean> predicate) {
    externalOperations.put(key, predicate);
  }

  public SearchCriteriaBuilder builder() {
    return new SearchCriteriaBuilder();
  }

  /**
   * Checks criteria for given fields.
   * <br>
   * Each key in {@code args} will be matched with keys in query.
   *
   * @param args Args to check criteria on
   * @return true - if {@code args} match this criteria
   */
  @Override
  public boolean test(Map<String, Object> args) {
    try {
      return andBlocks
          .stream()
          .allMatch(searchCriteriaBuilder -> testBlock(searchCriteriaBuilder, args));
    } catch (NullPointerException e) {
      throw new CriteriaException(
          "Exception occurred during search process! Check fields in search query. Actual fields: "
              + args.keySet()
      );
    }
  }

  private boolean testBlock(SearchCriteriaBuilder searchCriteriaBuilder, Map<String, Object> args) {
    return searchCriteriaBuilder.getCriterias()
        .stream()
        .anyMatch(criteriaPredicate -> criteriaPredicate.predicate()
            .test(args.get(criteriaPredicate.arg())));
  }

  private void processOperation(SearchCriteriaBuilder searchCriteriaBuilder, String argName,
                                String code,
                                int argIndex, Object... args) {
    switch (code) {
      case "=":
        searchCriteriaBuilder.or(o -> o.equals(args[argIndex]), argName);
        break;
      case ">":
        searchCriteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) > 0, argName);
        break;
      case ">=":
        searchCriteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) > -1, argName);
        break;
      case "<":
        searchCriteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) < 0, argName);
        break;
      case "<=":
        searchCriteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) <= 0, argName);
        break;
      case "?=":
        searchCriteriaBuilder.<String> or(o -> fuzzySearch(args[argIndex].toString(), o), argName);
        break;
      default:
        if (!externalOperations.containsKey(code)) {
          throw new CriteriaException("Unsupported search operation: " + code);
        }
        BiFunction<Object, Object, Boolean> predicate = externalOperations.get(code);
        searchCriteriaBuilder.or(o -> predicate.apply(o, args[argIndex]), argName);
    }
  }

  private void addBlock(SearchCriteriaBuilder block) {
    andBlocks.add(block);
  }

  private boolean fuzzySearch(String query, String term) {
    int index = term.indexOf(query.charAt(0));
    boolean found = index > -1;
    int indexOffset = index + 1;
    term = term.substring(index + 1);
    for (int i = 1; found && index != -1 && i < query.length(); i++) {
      int newIndex = term.indexOf(query.charAt(i));
      found = newIndex > -1 && newIndex + indexOffset >= index;
      term = term.substring(newIndex + 1);
      indexOffset += newIndex;
      index = indexOffset;
    }
    return found;
  }

  public class SearchCriteriaBuilder implements CriteriaBuilder {

    private final List<CriteriaPredicate> criterias;

    public SearchCriteriaBuilder() {
      this.criterias = new ArrayList<>();
    }

    public <T> SearchCriteriaBuilder or(Predicate<T> criteria, String arg) {
      criterias.add(new CriteriaPredicate(criteria, arg));
      return this;
    }

    public SearchCriteriaBuilder and() {
      addBlock(this);
      return new SearchCriteriaBuilder();
    }

    public SearchCriteria build() {
      if (criterias.size() > 0) {
        addBlock(this);
      }
      return SearchCriteria.this;
    }

    public List<CriteriaPredicate> getCriterias() {
      return criterias;
    }

    public SearchCriteria createFromExpression(String exp, Object... args) {
      Pattern expPattern = Pattern.compile("([\\w.]+) *(=|>=|<=|>|<|\\?=|[\\S]{1,3})++ *\\$(\\d+)");
      String[] andBlocks = exp.split("(?<!\\w)(AND|and|&)(?!\\w)");
      SearchCriteriaBuilder searchCriteriaBuilder = SearchCriteria.create();
      for (String block : andBlocks) {
        String[] orBlocks = block.split("(?<!\\w)(OR|or|\\|)(?!\\w)");
        for (String orBlock : orBlocks) {
          Matcher matcher = expPattern.matcher(orBlock.trim());
          if (matcher.find()) {
            String argName = matcher.group(1);
            int argIndex = Integer.parseInt(matcher.group(3));
            String code = matcher.group(2).trim();
            processOperation(searchCriteriaBuilder, argName, code, argIndex, args);
          }
        }
        searchCriteriaBuilder = searchCriteriaBuilder.and();
      }
      return searchCriteriaBuilder.build();
    }
  }
}
