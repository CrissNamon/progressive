package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.basic.BasicGame;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface GameComponentInstantiator {
    static  <C> C instantiate(Class<C> componentClass, Object... args) {
        try {
            Class<?>[] argsTypes = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                argsTypes[i] = args[i].getClass();
            }
            BasicGame game = BasicGame.getInstance();
            Constructor<?>[] constructors = componentClass.getDeclaredConstructors();
            for(Constructor<?> constructor : constructors) {
                if(constructor.isAnnotationPresent(Autofill.class)) {
                    args = new Object[constructor.getParameterCount()];
                    argsTypes = constructor.getParameterTypes();
                    Autofill autofill = constructor.getAnnotation(Autofill.class);
                    for(int i = 0; i < constructor.getParameterCount(); ++i) {
                        args[i] = game.getBean("", argsTypes[i]);
                    }
                    break;
                }
            }
            C instance = componentClass.getDeclaredConstructor(argsTypes).newInstance(args);
            Field[] fields = instance.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                if(f.isAnnotationPresent(Autofill.class)) {
                    Autofill autofill = f.getAnnotation(Autofill.class);
                    f.set(instance, game.getBean(autofill.value(), f.getType()));
                }
            }
            return instance;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not instantiate component " + componentClass.getName() + "! Component must have non-private constructor!");
        }
    }
}
