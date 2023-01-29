package tech.hiddenproject.progressive;

import java.lang.annotation.Annotation;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Danila Rassokhin
 */
public interface ComponentCreator {

  <C> C create(Class<C> componentClass, Object... args);

  List<Constructor<?>> foundAutoInjectConstructors(Constructor<?>[] constructors);

  Object[] injectBeansToParameters(Class<?> beanClass, Class<?>[] parameterTypes,
                                   Annotation[][] parameterAnnotations);

  void setIsHandlesEnabled(boolean isHandlesEnabled);

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

  default Class<?>[] getArgsTypes(Object... args) {
    Class<?>[] argsTypes = new Class[args.length];
    for (int i = 0; i < args.length; ++i) {
      argsTypes[i] = args[i].getClass();
    }
    return argsTypes;
  }

  default Object invokeObjectMethod(Object bean, Method method) throws Throwable {
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

  default Object invokeObjectMethodWithOneParam(Object bean, Method method, Object arg)
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

  default boolean isHandlesEnabled() {
    return true;
  }

}
