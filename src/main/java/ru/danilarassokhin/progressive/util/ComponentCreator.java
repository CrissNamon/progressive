package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.basic.injection.BasicDIContainer;
import ru.danilarassokhin.progressive.exception.BeanNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Creates components from their classes
 */
public final class ComponentCreator {
    /**
     * Creates object from given class and makes autoinjection for fields and methods if they annotated as @Autofill
     * @param componentClass Object class to instantiate
     * @param args Parameters to pass in class constructor
     * @param <C> Object to instantiate
     * @return Instantiated object of null
     */
    public static <C> C create(Class<C> componentClass, Object... args) {
        try {
            Class<?>[] argsTypes = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                argsTypes[i] = args[i].getClass();
            }
            C instance = null;
            BasicDIContainer diContainer = BasicDIContainer.getInstance();
            Constructor<?>[] constructors = componentClass.getDeclaredConstructors();
            for(Constructor<?> constructor : constructors) {
                if(constructor.isAnnotationPresent(Autofill.class)) {
                    args = new Object[constructor.getParameterCount()];
                    argsTypes = constructor.getParameterTypes();
                    Autofill autofill = constructor.getAnnotation(Autofill.class);
                    String[] qualifiers = autofill.qualifiers();
                    for(int i = 0; i < constructor.getParameterCount(); ++i) {
                        if(qualifiers.length == constructor.getParameterCount()) {
                            args[i] = diContainer.getBean(qualifiers[i], argsTypes[i]);
                        }else{
                            args[i] = diContainer.getBean(argsTypes[i].getName().toLowerCase(), argsTypes[i]);
                        }
                    }
                    constructor.setAccessible(true);
                    instance = (C) constructor.newInstance(args);
                    break;
                }
            }
            if(instance == null) {
                Constructor<C> constructor = componentClass.getDeclaredConstructor(argsTypes);
                constructor.setAccessible(true);
                instance = constructor.newInstance(args);
            }
            Field[] fields = instance.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                if(f.isAnnotationPresent(Autofill.class)) {
                    Autofill autofill = f.getAnnotation(Autofill.class);
                    String name = autofill.value();
                    if(name.isEmpty()) {
                        name = f.getName().toLowerCase();
                    }
                    f.set(instance, diContainer.getBean(name, f.getType()));
                }
            }
            Method[] methods = instance.getClass().getDeclaredMethods();
            Arrays.sort(methods, Comparator.comparingInt(Method::getParameterCount));
            for(Method m : methods) {
                m.setAccessible(true);
                if(m.isAnnotationPresent(Autofill.class)) {
                    Autofill autofill = m.getAnnotation(Autofill.class);
                    String[] names = autofill.qualifiers();
                    argsTypes = m.getParameterTypes();
                    args = new Object[argsTypes.length];
                    for(int i = 0; i < m.getParameterCount(); ++i) {
                        try {
                            if (names.length != argsTypes.length) {
                                args[i] = diContainer.tryGetBean(argsTypes[i]);
                            } else {
                                args[i] = diContainer.tryGetBean(names[i], argsTypes[i]);
                            }
                        }catch (BeanNotFoundException e) {
                            throw new RuntimeException("Could not Autofill method " + m.getName() + " in " + instance.getClass().getName()
                                    + "! Beans for " + argsTypes[i].getName() + " not found...");
                        }
                    }
                    invoke(m, instance, args);
                }
            }
            return instance;
        }catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not instantiate component " + componentClass.getName() + "! Exception: " + e.getMessage());
        }catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not instantiate component " + componentClass.getName() + "! Component must have such a constructor: " + e.getMessage());
        }
    }

    /**
     * Invokes method from given object and given args
     * @param method Method to invoke
     * @param from Object to invoke method from
     * @param args Parameters to invoke method with
     */
    public static void invoke(Method method, Object from, Object... args) {
        try {
            method.invoke(from, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while method " + method.getName() + " invocation! Exception: " + e.getMessage());
        }
    }

    /**
     * Check if given modifier is included in all modifiers
     * <p>You can check if method or field you got from Reflection have some modifiers like private, public, etc</p>
     * @param allModifiers All modifiers field or method has
     * @param specificModifier Modifier to check
     * @return true if given modifier presented in all modifiers
     */
    public static boolean isModifierSet(int allModifiers, int specificModifier) {
        return (allModifiers & specificModifier) > 0;
    }
}
