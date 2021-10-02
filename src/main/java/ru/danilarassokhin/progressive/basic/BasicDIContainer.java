package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicDIContainer implements DIContainer {

    private static BasicDIContainer INSTANCE;
    private final Map<Class<?>, Map<String, Bean>> beans;
    private Set<Method> viewedMethods;

    private BasicDIContainer() {
        beans = new HashMap<>();
    }

    public static BasicDIContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasicDIContainer();
        }
        return INSTANCE;
    }

    private void scanPackage(String name) {
        Set<Class> classesInPackage = findAllClassesUsingClassLoader(name);
        for (Class c : classesInPackage) {
            loadBeanFrom(c);
        }
    }

    private void loadBeanFrom(Class<?> beanClass) {
        viewedMethods = new HashSet<>();
        try {
            GameBean annotation = ComponentAnnotationProcessor.findAnnotation(beanClass, GameBean.class);
            if (annotation != null) {
                String name = annotation.name();
                if (name.isEmpty()) {
                    name = beanClass.getName().toLowerCase();
                }
                System.out.println("Found GameBean annotation in " + beanClass.getName() + ". Trying to make bean...");
                Object o = null;
                if (annotation.policy().equals(GameBeanCreationPolicy.SINGLETON)) {
                    o = beanClass.getDeclaredConstructor().newInstance();
                }
                Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
                Bean b = new Bean(o, annotation.policy());
                if (beansOfClass.containsKey(name)) {
                    throw new RuntimeException("GameBean name duplication " + name + " in " + beanClass.getName());
                }
                beansOfClass.putIfAbsent(name, b);
                beans.putIfAbsent(beanClass, beansOfClass);
                System.out.println("GameBean with name " + name + "created for " + beanClass.getName());
            } else {
                Method[] methods = beanClass.getDeclaredMethods();
                for (Method m : methods) {
                    if (viewedMethods.contains(m)) {
                        continue;
                    }
                    if (m.isAnnotationPresent(GameBean.class)) {
                        System.out.println("Found GameBean annotation in " + beanClass.getName() + " in method " + m.getName() + ". Trying to make bean...");
                        annotation = m.getAnnotation(GameBean.class);
                        String name = annotation.name();
                        if (name.isEmpty()) {
                            name = m.getName().toLowerCase();
                        }
                        if (!m.getReturnType().equals(Void.TYPE)) {
                            Map<String, Bean> beansOfClass = beans.getOrDefault(m.getReturnType(), new HashMap<>());
                            Object o = ComponentCreator.create(beanClass);
                            Object result = null;
                            if (annotation.policy().equals(GameBeanCreationPolicy.SINGLETON)) {
                                result = invoke(m, o);
                            }
                            Bean b = new Bean(result, annotation.policy());
                            if (beansOfClass.containsKey(name)) {
                                throw new RuntimeException("GameBean name duplication " + name + " in " + beanClass.getName());
                            }
                            if (!beansOfClass.containsKey(name)) {
                                beansOfClass.putIfAbsent(name, b);
                                beans.putIfAbsent(m.getReturnType(), beansOfClass);
                                System.out.println("GameBean with name " + name + " created for " + m.getReturnType().getName() + " from method " + m.getName());
                            }
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(beanClass.getName() + " annotated as GameBean has no public empty constructor which is required for beans! Exception: " + e.getMessage());
        }
    }

    private void createBeanFromMethod(Method m, Object o) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (!m.getReturnType().equals(Void.TYPE)) {
            GameBean annotation = m.getAnnotation(GameBean.class);
            String name = annotation.name();
            if (name.isEmpty()) {
                name = m.getName().toLowerCase();
            }
            Map<String, Bean> beansOfClass = beans.getOrDefault(m.getReturnType(), new HashMap<>());
            if (m.getParameterCount() > 0) {
                invoke(m, o);
            }
            Object result = m.invoke(o);
            viewedMethods.add(m);
            Bean b = new Bean(result, annotation.policy());
            if (beansOfClass.containsKey(name)) {
                throw new RuntimeException("GameBean name duplication " + name + " in " + o.getClass().getName());
            }
            beansOfClass.putIfAbsent(name, b);
            beans.putIfAbsent(result.getClass(), beansOfClass);
            System.out.println("GameBean with name " + name + " created for " + result.getClass().getName() + " from method " + m.getName());
        }
    }

    private Object invoke(Method m, Object obj) throws InvocationTargetException, IllegalAccessException, ArrayIndexOutOfBoundsException, NoSuchMethodException {
        Class<?>[] paramTypes = m.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        String[] names = m.getAnnotation(GameBean.class).qualifiers();
        for (int i = 0; i < paramTypes.length; ++i) {
            if (!beans.containsKey(paramTypes[i])) {
                createBeanFromMethod(obj.getClass().getMethod(names[i]), obj);
            }
            args[i] = getBean(names[i].toLowerCase(), paramTypes[i]);
        }
        viewedMethods.add(m);
        return m.invoke(obj, args);
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
        if (!beans.containsKey(beanClass)) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        Bean b = beans.getOrDefault(beanClass, new HashMap<>()).getOrDefault(name, null);
        if (b == null) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        V exists = (V) b.getBean();
        if (exists == null) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        if (b.getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
            exists = ComponentCreator.create(beanClass);
            b.setBean(exists);
            beans.get(beanClass).put(name, b);
        }
        return exists;
    }

    public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config) {
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = config.getAnnotation(ComponentScan.class);
            for (String p : componentScan.value()) {
                scanPackage(p);
            }
        }
        loadBeanFrom(config);
    }
}
