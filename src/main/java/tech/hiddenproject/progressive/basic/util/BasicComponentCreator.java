package tech.hiddenproject.progressive.basic.util;

import java.lang.annotation.Annotation;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import tech.hiddenproject.progressive.annotation.Autofill;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.annotation.Qualifier;
import tech.hiddenproject.progressive.basic.BasicComponentManager;
import tech.hiddenproject.progressive.exception.BeanConflictException;
import tech.hiddenproject.progressive.injection.DIContainer;

/**
 * Instantiates objects from their classes and uses {@link DIContainer} for auto injection.
 */
public abstract class BasicComponentCreator {

  private static boolean isHandlesEnabled = true;

  /**
   * Defines if ComponentCreator can use {@link java.lang.invoke.MethodHandles} apis.
   *
   * @param isHandlesEnabled if true will use {@link java.lang.invoke.MethodHandles} to invoke methods or use
   *                         {@link java.lang.reflect.Method#invoke(Object, Object...)} otherwise
   */
  public static void setIsHandlesEnabled(boolean isHandlesEnabled) {
    BasicComponentCreator.isHandlesEnabled = isHandlesEnabled;
  }

  /**
   * Creates object from given class and makes auto injection for fields and methods if they annotated as @Autofill.
   *
   * @param componentClass Object class to instantiate
   * @param args           Parameters to pass in class constructor
   * @param <C>            Object to instantiate
   * @return Instantiated object of null
   */
  public static <C> C create(Class<C> componentClass, Object... args) {
    Proxy proxy = componentClass.getAnnotation(Proxy.class);
    if (proxy != null) {
      componentClass = BasicComponentManager.getProxyCreator().createProxyClass(componentClass);
    }
    try {
      Class<?>[] argsTypes = new Class[args.length];
      for (int i = 0; i < args.length; ++i) {
        argsTypes[i] = args[i].getClass();
      }
      C instance;
      DIContainer diContainer = BasicComponentManager.getDiContainer();
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
        Constructor<?> constructor = autoInjectConstructors.get(0);
        args =
            injectBeansToParameters(
                componentClass,
                constructor.getParameterTypes(),
                constructor.getParameterAnnotations()
            );
        constructor.setAccessible(true);
        instance = (C) constructor.newInstance(args);
      } else {
        Constructor<C> constructor = componentClass.getDeclaredConstructor(argsTypes);
        constructor.setAccessible(true);
        instance = constructor.newInstance(args);
      }
      Field[] fields = instance.getClass().getDeclaredFields();
      for (Field f : fields) {
        f.setAccessible(true);
        if (f.isAnnotationPresent(Autofill.class)) {
          Qualifier qualifier = f.getAnnotation(Qualifier.class);
          String name = f.getName().toLowerCase();
          if (qualifier != null) {
            name = qualifier.value();
          }
          f.set(instance, diContainer.getBean(name, f.getType()));
        }
      }
      Method[] methods = instance.getClass().getDeclaredMethods();
      Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
      for (Method m : methods) {
        m.setAccessible(true);
        if (m.isAnnotationPresent(Autofill.class)) {
          args =
              injectBeansToParameters(
                  componentClass, m.getParameterTypes(), m.getParameterAnnotations());
          invoke(m, instance, args);
        }
      }
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

  public static List<Constructor<?>> foundAutoInjectConstructors(Constructor<?>[] constructors) {
    return Arrays.stream(constructors)
        .parallel()
        .unordered()
        .filter(c -> c.isAnnotationPresent(Autofill.class))
        .collect(Collectors.toList());
  }

  public static Object[] injectBeansToParameters(
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
   * Invokes method from given object and given args.
   *
   * @param method Method to invoke
   * @param from   Object to invoke method from
   * @param args   Parameters to invoke method with
   */
  public static Object invoke(Method method, Object from, Object... args) {
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

  public static Qualifier findQualifierAnnotation(Class<?> beanClass, Annotation[] annotations) {
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

  private static Object invokeWithoutHandles(Method method, Object from, Object... args) {
    try {
      method.setAccessible(true);
      return method.invoke(from, args);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      throw new RuntimeException(
          "Exception has occurred while method invocation!"
              + " Exception: "
              + throwable.getMessage());
    }
  }

  private static Object invokeWithHandles(Method method, Object from, Object... args) {
    try {
      MethodHandles.Lookup caller = MethodHandles.lookup();
      List<Object> castedArgs = new ArrayList<>();
      castedArgs.add(from);
      castedArgs.addAll(Arrays.asList(args));
      method.setAccessible(true);
      if (args.length == 0 && method.getReturnType() != void.class) {
        return invokeObjectMethod(from, method);
      } else if (args.length == 1 && method.getReturnType() != void.class) {
        return invokeObjectMethodWithOneParam(from, method, args[0]);
      } else {
        return caller.unreflect(method).invokeWithArguments(castedArgs);
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      throw new RuntimeException(
          "Exception has occurred while method invocation!"
              + " Exception: "
              + throwable.getMessage());
    }
  }

  private static Object invokeObjectMethod(Object bean, Method method) throws Throwable {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    MethodType invokedType = MethodType.methodType(Function.class);
    method.setAccessible(true);
    MethodType func = caller.unreflect(method).type();
    CallSite site =
        LambdaMetafactory.metafactory(
            caller,
            "apply",
            invokedType,
            func.generic(),
            caller.unreflect(method),
            MethodType.methodType(Object.class, bean.getClass())
        );
    Function<Object, Object> fullFunction =
        (Function<Object, Object>) site.getTarget().invokeExact();
    return fullFunction.apply(bean);
  }

  private static Object invokeObjectMethodWithOneParam(Object bean, Method method, Object arg)
      throws Throwable {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    MethodType invokedType = MethodType.methodType(BiFunction.class);
    method.setAccessible(true);
    MethodType func = caller.unreflect(method).type();
    Class<?>[] methodParamTypes = method.getParameterTypes();
    CallSite site =
        LambdaMetafactory.metafactory(
            caller,
            "apply",
            invokedType,
            func.generic(),
            caller.unreflect(method),
            caller.unreflect(method).type()
        );
    BiFunction<Object, Object, Object> fullFunction =
        (BiFunction<Object, Object, Object>) site.getTarget().invoke();
    return fullFunction.apply(bean, methodParamTypes[0].cast(arg));
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
  public static boolean isModifierSet(int allModifiers, int specificModifier) {
    return (allModifiers & specificModifier) > 0;
  }
}
