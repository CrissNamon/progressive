package tech.hiddenproject.progressive;

import java.lang.annotation.Annotation;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Creates instances of classes and provides useful utils.
 *
 * @author Danila Rassokhin
 */
public interface ComponentCreator {

  /**
   * Creates class instance for given args. In
   * {@link tech.hiddenproject.progressive.util.SimpleComponentCreator} calls class' constructor
   * with given args.
   *
   * @param componentClass Class to create instance of
   * @param args           Args to pass in constructor
   * @param <C>            Class type
   * @return Instance of {@code componentClass}
   */
  <C> C create(Class<C> componentClass, Object... args);

  /**
   * Creates arguments for given method parameter types and their annotations.
   *
   * @param beanClass            Method holder
   * @param parameterTypes       Types of arguments
   * @param parameterAnnotations Annotations
   * @return Instances of {@code parameterTypes}
   */
  Object[] injectBeansToParameters(Class<?> beanClass, Class<?>[] parameterTypes,
                                   Annotation[][] parameterAnnotations);

  /**
   * Enables or disables usage of {@link MethodHandles}.
   *
   * @param isHandlesEnabled if true then {@link ComponentCreator} should use {@link MethodHandles}
   *                         or method invoking instead of standard {@link Method}
   */
  void setIsHandlesEnabled(boolean isHandlesEnabled);

  /**
   * Invokes given method from given object with given args.
   *
   * @param method {@link Method}
   * @param from   Owner of method
   * @param args   Arguments to pass as method arguments
   * @return Method call result
   */
  default Object invoke(Method method, Object from, Object... args) {
    try {
      method.setAccessible(true);
      if (!isHandlesEnabled()) {
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

  /**
   * Gets types for given arguments.
   *
   * @param args Arguments
   * @return Types of given arguments
   */
  default Class<?>[] getArgsTypes(Object... args) {
    Class<?>[] argsTypes = new Class[args.length];
    for (int i = 0; i < args.length; ++i) {
      argsTypes[i] = args[i].getClass();
    }
    return argsTypes;
  }

  default Function createGetter(final MethodHandles.Lookup caller,
                                final MethodHandle getter) throws Exception {
    final CallSite site = LambdaMetafactory.metafactory(caller, "apply",
                                                        MethodType.methodType(Function.class),
                                                        MethodType.methodType(
                                                            Object.class,
                                                            Object.class
                                                        ),
                                                        getter,
                                                        getter.type()
    );
    try {
      return (Function) site.getTarget().invokeExact();
    } catch (final Exception e) {
      throw e;
    } catch (final Throwable e) {
      throw new Error(e);
    }
  }

  default BiConsumer createSetter(final MethodHandles.Lookup caller,
                                  final MethodHandle setter) throws Exception {

    final CallSite site = LambdaMetafactory.metafactory(
        caller,
        "accept",
        MethodType.methodType(BiConsumer.class),
        MethodType.methodType(
            void.class,
            Object.class,
            Object.class
        ),
        setter,
        setter.type()
    );
    try {
      return (BiConsumer) site.getTarget().invokeExact();
    } catch (final Exception e) {
      throw e;
    } catch (final Throwable e) {
      throw new Error(e);
    }
  }

  default void invokeSetter(Object bean, Method m, Object arg) throws Exception {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    createSetter(caller, caller.unreflect(m)).accept(bean, arg);
  }

  default Object invokeGetter(Object bean, Method m) throws Exception {
    MethodHandles.Lookup caller = MethodHandles.lookup();
    return createGetter(caller, caller.unreflect(m)).apply(bean);
  }

  @Deprecated
  default Object invokeObjectMethod(Object bean, Method method) throws Throwable {
    return invokeGetter(bean, method);
  }

  default Object invokeWithoutHandles(Method method, Object from, Object... args) {
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

  default Object invokeWithHandles(Method method, Object from, Object... args) {
    try {
      MethodHandles.Lookup caller = MethodHandles.lookup();
      List<Object> castedArgs = new ArrayList<>();
      castedArgs.add(from);
      castedArgs.addAll(Arrays.asList(args));
      method.setAccessible(true);
      if (args.length == 0 && method.getReturnType() != void.class) {
        return invokeGetter(from, method);
      } else if (args.length == 1 && method.getReturnType() == void.class) {
        invokeSetter(from, method, args[0]);
        return null;
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

  default boolean isHandlesEnabled() {
    return true;
  }

}
