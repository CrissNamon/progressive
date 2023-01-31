package tech.hiddenproject.progressive.injection;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.ComponentCreator;
import tech.hiddenproject.progressive.annotation.Autofill;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.annotation.Qualifier;
import tech.hiddenproject.progressive.exception.BeanConflictException;

/**
 * Instantiates objects from their classes and uses {@link DIContainer} for auto injection.
 */
public class BasicComponentCreator implements ComponentCreator {

  private boolean isHandlesEnabled = true;

  /**
   * Creates object from given class and makes auto injection for fields and methods if they
   * annotated as @Autofill.
   *
   * @param componentClass Object class to instantiate
   * @param args           Parameters to pass in class constructor
   * @param <C>            Object to instantiate
   * @return Instantiated object of null
   */
  public <C> C create(Class<C> componentClass, Object... args) {
    Proxy proxy = componentClass.getAnnotation(Proxy.class);
    if (proxy != null) {
      componentClass = BasicComponentManager.getProxyCreator().createProxyClass(componentClass);
    }
    try {
      DIContainer diContainer = BasicComponentManager.getDiContainer();
      C instance = wireConstructors(componentClass, args);

      wireFields(instance, diContainer);

      wireMethods(instance);

      return instance;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      throw new RuntimeException(
          "Could not instantiate component "
              + componentClass.getName()
              + "! Exception: "
              + e.getMessage());
    } catch (NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException(
          "Could not instantiate component "
              + componentClass.getName()
              + "! Component must have such a method: "
              + e.getMessage());
    }
  }

  public Object[] injectBeansToParameters(
      Class<?> beanClass, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
    DIContainer diContainer = BasicComponentManager.getDiContainer();
    Object[] args = new Object[parameterTypes.length];
    for (int i = 0; i < args.length; ++i) {
      Qualifier qualifier = findQualifierAnnotation(beanClass, parameterAnnotations[i]);
      if (qualifier == null) {
        args[i] = diContainer.getBean(parameterTypes[i]);
      } else {
        args[i] = diContainer.getBean(qualifier.value(), parameterTypes[i]);
      }
    }
    return args;
  }

  /**
   * Defines if ComponentCreator can use {@link MethodHandles} apis.
   *
   * @param isHandlesEnabled if true will use {@link MethodHandles} to invoke methods or use
   *                         {@link Method#invoke(Object, Object...)} otherwise
   */
  public void setIsHandlesEnabled(boolean isHandlesEnabled) {
    this.isHandlesEnabled = isHandlesEnabled;
  }

  /**
   * Invokes method from given object and given args.
   *
   * @param method Method to invoke
   * @param from   Object to invoke method from
   * @param args   Parameters to invoke method with
   */
  public Object invoke(Method method, Object from, Object... args) {
    try {
      method.setAccessible(true);
      if (!isHandlesEnabled) {
        return invokeWithoutHandles(method, from, args);
      } else {
        return invokeWithHandles(method, from, args);
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      throw new RuntimeException(
          "Exception has occurred while method invocation!"
              + " Exception: "
              + throwable.getMessage());
    }
  }

  public List<Constructor<?>> foundAutoInjectConstructors(Constructor<?>[] constructors) {
    return Arrays.stream(constructors)
        .parallel()
        .unordered()
        .filter(c -> c.isAnnotationPresent(Autofill.class))
        .collect(Collectors.toList());
  }

  public Qualifier findQualifierAnnotation(Class<?> beanClass, Annotation[] annotations) {
    Qualifier qualifier = null;
    List<Annotation> qualifiers =
        Arrays.stream(annotations)
            .parallel()
            .unordered()
            .filter(a -> a.annotationType() == Qualifier.class)
            .collect(Collectors.toList());
    if (qualifiers.size() > 1) {
      throw new BeanConflictException(
          "Method parameter in "
              + beanClass.getName()
              + " has more than one @Qualifier annotation. What to use?");
    }
    if (qualifiers.size() == 1) {
      qualifier = (Qualifier) qualifiers.get(0);
    }
    return qualifier;
  }

  /**
   * Check if given modifier is included in all modifiers.
   *
   * <p>You can check if method or field you got from Reflection have some modifiers like private,
   * public, etc
   *
   * @param allModifiers     All modifiers field or method has
   * @param specificModifier Modifier to check
   * @return true if given modifier presented in all modifiers
   */
  public boolean isModifierSet(int allModifiers, int specificModifier) {
    return (allModifiers & specificModifier) > 0;
  }

  private <C> C wireConstructors(Class<C> componentClass, Object... args)
      throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    Constructor<?>[] constructors = componentClass.getDeclaredConstructors();
    Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));
    List<Constructor<?>> autoInjectConstructors = foundAutoInjectConstructors(constructors);
    if (autoInjectConstructors.size() > 1) {
      throw new BeanConflictException(
          "Found more than one constructor in "
              + componentClass.getName()
              + " annotated as @Autofill. What to use?");
    }
    if (autoInjectConstructors.size() == 1) {
      return wireConstructor(autoInjectConstructors.get(0), componentClass);
    }
    Constructor<C> constructor = componentClass.getDeclaredConstructor(getArgsTypes(args));
    constructor.setAccessible(true);
    return constructor.newInstance(args);
  }

  private <C> C wireConstructor(Constructor<?> constructor, Class<C> componentClass)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    Object[] args = injectBeansToParameters(
        componentClass,
        constructor.getParameterTypes(),
        constructor.getParameterAnnotations()
    );
    constructor.setAccessible(true);
    return (C) constructor.newInstance(args);
  }

  private <C> void wireMethods(C instance) {
    Method[] methods = instance.getClass().getDeclaredMethods();
    Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
    for (Method m : methods) {
      wireMethod(m, instance);
    }
  }

  private <C> void wireMethod(Method m, C instance) {
    m.setAccessible(true);
    if (m.isAnnotationPresent(Autofill.class)) {
      Object[] args =
          injectBeansToParameters(
              instance.getClass(), m.getParameterTypes(), m.getParameterAnnotations());
      invoke(m, instance, args);
    }
  }

  private <C> void wireFields(C instance, DIContainer diContainer)
      throws IllegalAccessException {
    Field[] fields = instance.getClass().getDeclaredFields();
    for (Field f : fields) {
      f.setAccessible(true);
      if (f.isAnnotationPresent(Autofill.class)) {
        wireField(f, instance, diContainer);
      }
    }
  }

  private <C> void wireField(Field f, C instance, DIContainer diContainer)
      throws IllegalAccessException {
    Qualifier qualifier = f.getAnnotation(Qualifier.class);
    String name = f.getName().toLowerCase();
    if (qualifier != null) {
      name = qualifier.value();
    }
    f.set(instance, diContainer.getBean(name, f.getType()));
  }
}
