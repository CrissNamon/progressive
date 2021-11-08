package ru.danilarassokhin.progressive.basic.injection;

import java.lang.reflect.Method;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;

/**
 * Represents bean for Dependency Injection container
 */
public final class Bean {

  private Object bean;
  private Method method;
  private Object[] methodArgs;
  private String[] methodArgsQualifiers;
  private Object methodCaller;
  private GameBeanCreationPolicy creationPolicy;
  private boolean isReady = true;
  private Class<?> realType;
  private boolean isCreated = false;

  public Object getBean() {
    return bean;
  }

  public GameBeanCreationPolicy getCreationPolicy() {
    return creationPolicy;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Object[] getMethodArgs() {
    return methodArgs;
  }

  public void setMethodArgs(Object[] methodArgs) {
    this.methodArgs = methodArgs;
  }

  public void setCreationPolicy(GameBeanCreationPolicy creationPolicy) {
    this.creationPolicy = creationPolicy;
  }

  public Object getMethodCaller() {
    return methodCaller;
  }

  public void setMethodCaller(Object methodCaller) {
    this.methodCaller = methodCaller;
  }

  public String[] getMethodArgsQualifiers() {
    return methodArgsQualifiers;
  }

  public void setMethodArgsQualifiers(String[] methodArgsQualifiers) {
    this.methodArgsQualifiers = methodArgsQualifiers;
  }

  public boolean isReady() {
    return isReady;
  }

  public void setReady(boolean ready) {
    isReady = ready;
  }

  public Class<?> getRealType() {
    return realType;
  }

  public void setRealType(Class<?> realType) {
    this.realType = realType;
  }

  public boolean isCreated() {
    return isCreated;
  }

  public void setCreated(boolean created) {
    isCreated = created;
  }

  public boolean isClass() {
    return getMethod() == null;
  }

  public boolean isMethod() {
    return getMethod() != null;
  }

  public boolean haveObject() {
    return getBean() != null;
  }
}
