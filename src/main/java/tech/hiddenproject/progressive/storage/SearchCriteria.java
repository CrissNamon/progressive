package tech.hiddenproject.progressive.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Danila Rassokhin
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SearchCriteria {

  private final List<CriteriaBuilder> andBlocks;

  public SearchCriteria() {
    this.andBlocks = new ArrayList<>();
  }

  public static CriteriaBuilder create() {
    return new SearchCriteria().builder();
  }

  public static SearchCriteria createFromExpression(String exp, Object... args) {
    return SearchCriteria.create().createFromExpression(exp, args);
  }

  public CriteriaBuilder builder() {
    return new CriteriaBuilder();
  }

  public boolean test(Map<String, Object> args) {
    try {
      return andBlocks
          .stream()
          .allMatch(criteriaBuilder -> testBlock(criteriaBuilder, args));
    } catch (NullPointerException e) {
      throw new RuntimeException(
          "Exception occurred during search process! Check fields in search query. Actual fields: "
              + args.keySet()
      );
    }
  }

  private boolean testBlock(CriteriaBuilder criteriaBuilder, Map<String, Object> args) {
    return criteriaBuilder.getCriterias()
        .stream()
        .anyMatch(criteriaPredicate -> criteriaPredicate.predicate()
            .test(args.get(criteriaPredicate.arg())));
  }

  private void processOperation(CriteriaBuilder criteriaBuilder, String argName, String code,
                                int argIndex, Object... args) {
    switch (code) {
      case "=":
        criteriaBuilder.or(o -> o.equals(args[argIndex]), argName);
        break;
      case ">":
        criteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) > 0, argName);
        break;
      case ">=":
        criteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) > -1, argName);
        break;
      case "<":
        criteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) < 0, argName);
        break;
      case "<=":
        criteriaBuilder.<Comparable> or(o -> o.compareTo(args[argIndex]) <= 0, argName);
        break;
      case "?=":
        criteriaBuilder.<String> or(o -> fuzzySearch(args[argIndex].toString(), o), argName);
        break;
      default:
        throw new RuntimeException("Unsupported search operation: " + code);
    }
  }

  private void addBlock(CriteriaBuilder block) {
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

  public class CriteriaBuilder {

    private final List<CriteriaPredicate> criterias;

    public CriteriaBuilder() {
      this.criterias = new ArrayList<>();
    }

    public <T> CriteriaBuilder or(Predicate<T> criteria, String arg) {
      criterias.add(new CriteriaPredicate(criteria, arg));
      return this;
    }

    public CriteriaBuilder and() {
      addBlock(this);
      return new CriteriaBuilder();
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
      Pattern expPattern = Pattern.compile("([\\w.]+) *(=|>=|<=|>|<|\\?=)++ *\\$(\\d+)");
      String[] andBlocks = exp.split("(?<!\\w)(AND|and|&)(?!\\w)");
      CriteriaBuilder criteriaBuilder = SearchCriteria.create();
      for (String block : andBlocks) {
        String[] orBlocks = block.split("(?<!\\w)(OR|or|\\|)(?!\\w)");
        for (String orBlock : orBlocks) {
          Matcher matcher = expPattern.matcher(orBlock.trim());
          if (matcher.find()) {
            String argName = matcher.group(1);
            int argIndex = Integer.parseInt(matcher.group(3));
            String code = matcher.group(2).trim();
            processOperation(criteriaBuilder, argName, code, argIndex, args);
          }
        }
        criteriaBuilder = criteriaBuilder.and();
      }
      return criteriaBuilder.build();
    }
  }
}
