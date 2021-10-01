package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.component.GameComponent;

import java.lang.reflect.InvocationTargetException;

public interface GameComponentInstantiator {
    static  <C extends GameComponent> C instantiate(Class<C> componentClass, Object... args) {
        try {
            Class<?>[] argsTypes = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                argsTypes[i] = args[i].getClass();
            }
            return componentClass.getDeclaredConstructor(argsTypes).newInstance(args);
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not instantiate component " + componentClass.getName() + "! Component must have non-private constructor!");
        }
    }

    static  <C extends GameComponent> C instantiate(Class<C> componentClass) {
        return instantiate(componentClass);
    }
}
