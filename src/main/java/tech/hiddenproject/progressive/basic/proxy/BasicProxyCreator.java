package tech.hiddenproject.progressive.basic.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import tech.hiddenproject.progressive.annotation.Intercept;
import tech.hiddenproject.progressive.annotation.Proxy;
import tech.hiddenproject.progressive.basic.util.BasicComponentCreator;
import tech.hiddenproject.progressive.exception.BeanConflictException;
import tech.hiddenproject.progressive.proxy.MethodInterceptor;
import tech.hiddenproject.progressive.proxy.ProxyCreator;
import tech.hiddenproject.progressive.util.ComponentAnnotationProcessor;

/**
 * Creates proxy classes.
 */
public final class BasicProxyCreator implements ProxyCreator {

  private static final ClassLoadingStrategy DEFAULT_CLASS_LOADING_STRATEGY =
      ClassLoadingStrategy.Default.WRAPPER;

  private static BasicProxyCreator INSTANCE;

  private ClassLoadingStrategy classLoadingStrategy;

  private BasicProxyCreator() {
    setClassLoadingStrategy(DEFAULT_CLASS_LOADING_STRATEGY);
  }

  /**
   * Returns instance of {@link BasicProxyCreator}.
   *
   * @return instance of {@link BasicProxyCreator}
   */

  public static BasicProxyCreator getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BasicProxyCreator();
    }
    return INSTANCE;
  }

  /**
   * Sets {@link net.bytebuddy.dynamic.loading.ClassLoadingStrategy} for proxy generator.
   *
   * @param classLoadingStrategy strategy to use for proxy creation
   */
  public synchronized void setClassLoadingStrategy(ClassLoadingStrategy classLoadingStrategy) {
    this.classLoadingStrategy = classLoadingStrategy;
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original, MethodInterceptor interceptor) {
    List<Constructor<?>> autoInjectConstructors =
        BasicComponentCreator.foundAutoInjectConstructors(original.getDeclaredConstructors());
    if (autoInjectConstructors.size() > 1) {
      throw new BeanConflictException(
          "Found more than one constructor in "
              + original.getName()
              + " annotated as @Autofill. What to use?");
    }
    BasicProxyInterceptionHandler basicProxyInterceptionHandler =
        new BasicProxyInterceptionHandler(interceptor);
    DynamicType.Builder builder = createDynamicType(original);

    if (autoInjectConstructors.size() == 1) {
      Constructor<?> constructor = autoInjectConstructors.get(0);
      DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition receiverTypeDefinition =
          createReceiverTypeDefinition(
              ElementMatchers.isDeclaredBy(original),
              ElementMatchers.named(constructor.getName()),
              basicProxyInterceptionHandler,
              builder
          );
      builder = copyAutofillConstructor(constructor, receiverTypeDefinition);
    } else {
      builder =
          createReceiverTypeDefinition(
              ElementMatchers.isDeclaredBy(original),
              ElementMatchers.isConstructor(),
              basicProxyInterceptionHandler,
              builder
          );
    }
    DynamicType.Unloaded<V> unloadedClass = makeProxy(builder);
    Class<?> type =
        loadProxyClass(unloadedClass, original.getClassLoader(), getInstance().classLoadingStrategy)
            .getLoaded();
    return (Class<V>) type;
  }

  @Override
  public <V> V createProxy(Class<V> original, MethodInterceptor interceptor, Object... args) {
    return BasicComponentCreator.create(createProxyClass(original, interceptor), args);
  }

  @Override
  public <V> Class<V> createProxyClass(Class<V> original) {
    Proxy proxy = ComponentAnnotationProcessor.findAnnotation(original, Proxy.class);
    if (proxy == null) {
      throw new RuntimeException(
          original.getName()
              + " must be annotated as @Proxy or MethodInterceptor must be specified!");
    }
    MethodInterceptor interceptor = BasicComponentCreator.create(proxy.value());
    BasicProxyInterceptionHandler basicProxyInterceptionHandler =
        new BasicProxyInterceptionHandler(interceptor);
    List<Constructor<?>> autoInjectConstructors =
        BasicComponentCreator.foundAutoInjectConstructors(original.getDeclaredConstructors());
    if (autoInjectConstructors.size() > 1) {
      throw new BeanConflictException(
          "Found more than one constructor in "
              + original.getName()
              + " annotated as @Autofill. What to use?");
    }
    DynamicType.Builder builder = createDynamicType(original);
    if (autoInjectConstructors.size() == 1) {
      Constructor<?> constructor = autoInjectConstructors.get(0);
      DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition receiverTypeDefinition =
          createReceiverTypeDefinition(
              ElementMatchers.isAnnotatedWith(Intercept.class),
              ElementMatchers.named(constructor.getName()),
              basicProxyInterceptionHandler,
              builder
          );
      builder = copyAutofillConstructor(constructor, receiverTypeDefinition);
    } else {
      builder =
          createReceiverTypeDefinition(
              ElementMatchers.isAnnotatedWith(Intercept.class),
              ElementMatchers.isConstructor(),
              basicProxyInterceptionHandler,
              builder
          );
    }
    DynamicType.Unloaded<V> unloadedClass = makeProxy(builder);
    Class<?> type =
        loadProxyClass(unloadedClass, original.getClassLoader(), getInstance().classLoadingStrategy)
            .getLoaded();
    return (Class<V>) type;
  }

  @Override
  public <V> V createProxy(Class<V> original, Object... args) {
    return BasicComponentCreator.create(createProxyClass(original), args);
  }

  private DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition createReceiverTypeDefinition(
      ElementMatcher elementMatcherMethod,
      ElementMatcher elementMatcherConstructor,
      BasicProxyInterceptionHandler basicProxyInterceptionHandler,
      DynamicType.Builder builder) {
    return builder
        .method(elementMatcherMethod)
        .intercept(MethodDelegation.to(basicProxyInterceptionHandler))
        .constructor(elementMatcherConstructor)
        .intercept(SuperMethodCall.INSTANCE)
        .method(ElementMatchers.isStatic())
        .intercept(SuperMethodCall.INSTANCE);
  }

  private DynamicType.Builder copyAutofillConstructor(
      Constructor<?> constructor,
      DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition receiverTypeDefinition) {
    DynamicType.Builder.MethodDefinition methodDefinition = receiverTypeDefinition;
    Annotation[][] annotations = constructor.getParameterAnnotations();
    for (int i = 0; i < constructor.getParameterCount(); ++i) {
      for (int j = 0; j < annotations[i].length; ++j) {
        methodDefinition = methodDefinition.annotateParameter(i, annotations[i][j]);
      }
    }
    return methodDefinition;
  }

  private <V> DynamicType.Builder<V> createDynamicType(Class<V> original) {
    return new ByteBuddy()
        .subclass(
            original, ConstructorStrategy.Default.IMITATE_SUPER_CLASS.withInheritedAnnotations());
  }

  private <V> DynamicType.Unloaded<V> makeProxy(DynamicType.Builder<V> builder) {
    return builder.make();
  }

  private <V> DynamicType.Loaded<V> loadProxyClass(
      DynamicType.Unloaded<V> unloadedClass,
      ClassLoader classLoader,
      ClassLoadingStrategy classLoadingStrategy) {
    return unloadedClass.load(classLoader, classLoadingStrategy);
  }
}
