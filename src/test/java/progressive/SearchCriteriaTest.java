package progressive;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.basic.storage.SearchCriteria;
import tech.hiddenproject.progressive.exception.CriteriaException;

/**
 * @author Danila Rassokhin
 */
public class SearchCriteriaTest {

  @Test
  public void validCriteriaTest() {
    SearchCriteria searchCriteria = SearchCriteria.create()
        .<Integer> or(o -> o == 1, "a")
        .<String> or(o -> o.equals("Hello"), "b")
        .and()
        .or(Objects::nonNull, "c")
        .build();

    Map<String, Object> testData1 = new HashMap<>();
    testData1.put("a", 1);
    testData1.put("b", "Hello");
    testData1.put("c", new Object());

    Map<String, Object> testData2 = new HashMap<>();
    testData2.put("a", 2);
    testData2.put("b", "Hello");
    testData2.put("c", new Object());

    Map<String, Object> testData3 = new HashMap<>();
    testData3.put("a", 1);
    testData3.put("b", "WRONG");
    testData3.put("c", new Object());

    Map<String, Object> testData4 = new HashMap<>();
    testData4.put("a", 1);
    testData4.put("b", "Hello");
    testData4.put("c", null);

    Assertions.assertTrue(searchCriteria.test(testData1));
    Assertions.assertTrue(searchCriteria.test(testData2));
    Assertions.assertTrue(searchCriteria.test(testData3));
    Assertions.assertFalse(searchCriteria.test(testData4));
  }

  @Test
  public void invalidCriteriaTest() {
    SearchCriteria searchCriteria = SearchCriteria.createFromExpression("name = $0", "Hi");

    Map<String, Object> testData = new HashMap<>();
    testData.put("wrong", null);

    Assertions.assertThrows(CriteriaException.class, () -> searchCriteria.test(testData));
  }


}
