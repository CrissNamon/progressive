package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.annotation.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.GameAnnotationProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BasicGame implements Game {

    private static BasicGame INSTANCE;

    private Map<Long, GameNode> gameNodes;
    private Map<Long, GameObject> gameObjects;
    private BasicGameStateManager stateManager;
    private List<Object> gameBeans;
    private Map<Class<?>, Map<String, Bean>> beans;

    private BasicGame() {
        gameNodes = new HashMap<>();
        gameObjects = new HashMap<>();
        gameBeans = new ArrayList<>();
        beans = new HashMap<>();
        stateManager = BasicGameStateManager.getInstance();
        stateManager.setState(GameState.INIT, this);
    }

    public static BasicGame getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGame();
        }
        return INSTANCE;
    }

    @Override
    public GameObject addGameObject() {
        Long lastId = gameObjects.keySet().stream().max(Long::compareTo).orElse(0L);
        GameObject gameObject = new BasicGameObject(++lastId);
        gameObjects.putIfAbsent(lastId, gameObject);
        return gameObject;
    }

    private void scanPackage(String name) {
        Set<Class> classesInPackage = findAllClassesUsingClassLoader(name);
        for(Class c : classesInPackage) {
            loadBeanFrom(c);
        }
    }

    private void loadBeanFrom(Class<?> beanClass) {
        try {
            GameBean annotation = GameAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
            if (annotation != null) {
                    System.out.println("Found GameBean annotation in " + beanClass.getName() + ". Trying to make bean...");
                    Object o = beanClass.getDeclaredConstructor().newInstance();
                    Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
                    Bean b = new Bean(o, annotation.policy());
                    if(beansOfClass.containsKey(annotation.name())) {
                        throw new RuntimeException("GameBean name duplication " + annotation.name() + " in " + beanClass.getName());
                    }
                    beansOfClass.putIfAbsent(annotation.name(), b);
                    beans.putIfAbsent(beanClass, beansOfClass);
                    System.out.println("GameBean created for " + beanClass.getName() + "");
            }else{
                Method[] methods = beanClass.getDeclaredMethods();
                for(Method m : methods) {
                    if(m.isAnnotationPresent(GameBean.class)) {
                        annotation = m.getAnnotation(GameBean.class);
                        if(!m.getReturnType().equals(Void.TYPE)) {
                            Map<String, Bean> beansOfClass = beans.getOrDefault(m.getReturnType(), new HashMap<>());
                            Object o = beanClass.getDeclaredConstructor().newInstance();
                            Object result = m.invoke(o);
                            Bean b = new Bean(result, annotation.policy());
                            if(beansOfClass.containsKey(annotation.name())) {
                                throw new RuntimeException("GameBean name duplication " + annotation.name() + " in " + beanClass.getName());
                            }
                            beansOfClass.putIfAbsent(annotation.name(), b);
                            beans.putIfAbsent(result.getClass(), beansOfClass);
                        }
                    }
                }
            }
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(beanClass.getName() + " annotated as GameBean has no public empty constructor which is required for beans!");
        }
    }

    private Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class " + className + " not found!");
        }
    }

    public <V> V getBean(Class<V> beanClass) {
        return getBean("", beanClass);
    }

    public <V> V getBean(String name, Class<V> beanClass) {
        if(!beans.containsKey(beanClass)) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        Bean b = beans.getOrDefault(beanClass, new HashMap<>()).getOrDefault(name, null);
        if(b == null) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        V exists = (V) b.getBean();
        if(exists == null) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        try {
            if(b.getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
                exists = beanClass.getDeclaredConstructor().newInstance();
                b.setBean(exists);
                beans.get(beanClass).put(name, b);
            }
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(beanClass.getName() + " annotated as GameBean but doesn't have an empty public constructor!");
        }
        return exists;
    }

    public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config) {
        if(config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = config.getAnnotation(ComponentScan.class);
            for(String p : componentScan.value()) {
                scanPackage(p);
            }
        }
        loadBeanFrom(config);
    }
}
