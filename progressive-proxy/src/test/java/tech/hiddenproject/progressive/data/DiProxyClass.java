package tech.hiddenproject.progressive.data;

import tech.hiddenproject.progressive.annotation.Autofill;
import tech.hiddenproject.progressive.annotation.GameBean;
import tech.hiddenproject.progressive.annotation.Intercept;
import tech.hiddenproject.progressive.annotation.Proxy;

/**
 * @author Danila Rassokhin
 */
@GameBean
@Proxy(LoggerMethodInterceptor.class)
public class DiProxyClass {

  private Long id;

  @Autofill
  private Long autoId;

  @Autofill
  public DiProxyClass(Long id) {
    this.id = id;
  }

  @Intercept
  public Long getId() {
    return id;
  }

  public Long getAutoId() {
    return autoId;
  }

  public void setAutoId(Long id) {
    this.autoId = id;
  }
}
