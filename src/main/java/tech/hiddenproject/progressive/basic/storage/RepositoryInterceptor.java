package tech.hiddenproject.progressive.basic.storage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import tech.hiddenproject.progressive.annotation.Select;
import tech.hiddenproject.progressive.storage.DefaultStorageRepository;
import tech.hiddenproject.progressive.storage.StorageRepository;
import tech.hiddenproject.progressive.util.ClassProcessor;

/**
 * Intercepts method calls from any implementation of {@link StorageRepository}.
 *
 * @author Danila Rassokhin
 */
public class RepositoryInterceptor implements InvocationHandler {

  private final StorageRepository storageRepository;

  public RepositoryInterceptor(Class<? extends StorageRepository> realClass) {
    this.storageRepository = new DefaultStorageRepository(realClass);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return processMethod(method, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private Object processMethod(Method method, Object[] args) throws Throwable {
    if (method.isAnnotationPresent(Select.class)) {
      Select select = method.getAnnotation(Select.class);
      return processSelectMethod(select, method, args);
    }
    return method.invoke(storageRepository, args);
  }

  private Object processSelectMethod(Select select, Method method, Object[] args) {
    SearchCriteria searchCriteria = SearchCriteria.createFromExpression(select.value(), args);
    return convertResult(storageRepository.search(searchCriteria), method.getReturnType());
  }

  private Object convertResult(List result, Class to) {
    if (!isCollection(to) && result.size() > 1) {
      throw new RuntimeException("Result must be a collection!");
    }
    if (isCollection(to)) {
      return result;
    }
    if (isOptional(to)) {
      return createOptionalFrom(result);
    }
    if (isRawClass(to)) {
      return createRawFrom(result);
    }
    return result;
  }

  private boolean isCollection(Class subject) {
    return ClassProcessor.isCollection(subject);
  }

  private boolean isRawClass(Class subject) {
    return !ClassProcessor.isCollection(subject) && !subject.equals(Optional.class);
  }

  private boolean isOptional(Class subject) {
    return !ClassProcessor.isCollection(subject) && subject.equals(Optional.class);
  }

  private Optional createOptionalFrom(List data) {
    if (data.size() == 0) {
      return Optional.empty();
    }
    return Optional.of(data.get(0));
  }

  private Object createRawFrom(List data) {
    if (data.size() == 0) {
      return null;
    }
    return data.get(0);
  }
}
