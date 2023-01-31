package tech.hiddenproject.progressive.data;

import tech.hiddenproject.progressive.annotation.Configuration;
import tech.hiddenproject.progressive.annotation.GameBean;

/**
 * @author Danila Rassokhin
 */
@Configuration
public class TestConfiguration {

  @GameBean
  public Long getId() {
    return 2L;
  }

}
