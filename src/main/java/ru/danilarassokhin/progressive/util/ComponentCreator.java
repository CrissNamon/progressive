package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.basic.BasicDIContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates components from their classes
 */
public interface ComponentCreator {
    /**
     * Creates object from given class
     * @param componentClass Object class to instantiate
     * @param args Parameters to pass in class constructor
     * @param <C> Object to instantiate
     * @return Instantiated object of null
     */
    static  <C> C create(Class<C> componentClass, Object... args) {
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
                    for(int i = 0; i < constructor.getParameterCount(); ++i) {
                        args[i] = diContainer.getBean("", argsTypes[i]);
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
                    f.set(instance, diContainer.getBean(autofill.value(), f.getType()));
                }
            }
            return instance;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not instantiate component " + componentClass.getName() + "! Component must have non-private constructor!");
        }
    }
}
