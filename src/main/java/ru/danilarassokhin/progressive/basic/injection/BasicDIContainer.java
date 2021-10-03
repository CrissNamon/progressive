package ru.danilarassokhin.progressive.basic.injection;

import ru.danilarassokhin.progressive.annotation.ComponentScan;
import ru.danilarassokhin.progressive.annotation.Components;
import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.basic.configuration.TestConfiguration;
import ru.danilarassokhin.progressive.configuration.AbstractConfiguration;
import ru.danilarassokhin.progressive.injection.DIContainer;
import ru.danilarassokhin.progressive.injection.GameBeanCreationPolicy;
import ru.danilarassokhin.progressive.injection.PackageLoader;
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
    private Class<?> scanFrom;

    private BasicDIContainer() {
        beans = new HashMap<>();
    }

    public static BasicDIContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasicDIContainer();
        }
        return INSTANCE;
    }

    private void scanPackage(String name, PackageLoader loader) {
        Set<Class> classesInPackage = loader.findAllClassesIn(name);
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
                System.out.println("Found GameBean annotation in "
                        + beanClass.getName() + ". Trying to make bean...");
                Object o = null;
                if (annotation.policy().equals(GameBeanCreationPolicy.SINGLETON)) {
                    o = ComponentCreator.create(beanClass);
                }
                Map<String, Bean> beansOfClass = beans.getOrDefault(beanClass, new HashMap<>());
                Bean b = new Bean(o, annotation.policy());
                if (beansOfClass.containsKey(name)) {
                    throw new RuntimeException("GameBean name duplication "
                            + name + " in " + beanClass.getName());
                }
                beansOfClass.putIfAbsent(name, b);
                beans.putIfAbsent(beanClass, beansOfClass);
                System.out.println("GameBean with name " + name + " created for " + beanClass.getName());
                System.out.println();
            } else {
                Method[] methods = beanClass.getDeclaredMethods();
                for (Method m : methods) {
                    if (viewedMethods.contains(m)) {
                        continue;
                    }
                    if (m.isAnnotationPresent(GameBean.class)) {
                        System.out.println("Found GameBean annotation in " + beanClass.getName()
                                + " in method " + m.getName()
                                + ". Trying to make bean...");
                        Object o = ComponentCreator.create(beanClass);
                        createBeanFromMethod(m, o);
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(beanClass.getName()
                    + " annotated as GameBean has no public empty constructor " +
                    "which is required for beans! Exception: " + e.getMessage());
        }
    }

    private void createBeanFromMethod(Method m, Object o) throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        if (!m.getReturnType().equals(Void.TYPE)) {
            GameBean annotation = m.getAnnotation(GameBean.class);
            String name = annotation.name();
            if (name.isEmpty()) {
                name = m.getName().toLowerCase();
            }
            Map<String, Bean> beansOfClass = beans.getOrDefault(
                    m.getReturnType(), new HashMap<>()
            );
            if (m.getParameterCount() > 0) {
                invoke(m, o);
            }
            Object result = m.invoke(o);
            viewedMethods.add(m);
            Bean b = new Bean(result, annotation.policy());
            if (beansOfClass.containsKey(name)) {
                throw new RuntimeException("GameBean name duplication "
                        + name + " in " + o.getClass().getName());
            }
            beansOfClass.putIfAbsent(name, b);
            beans.putIfAbsent(result.getClass(), beansOfClass);
            System.out.println("GameBean with name " + name + " created for "
                    + result.getClass().getName() + " from method " + m.getName());
            System.out.println();
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
        try {
            InputStream stream = getDefaultClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .collect(Collectors.toSet());
        }catch (NullPointerException e) {
            throw new RuntimeException("ClassLoader is not available! Load configuration through you own PackageLoader with loadConfiguration(config, loader)");
        }
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
        if (exists == null && b.getCreationPolicy().equals(GameBeanCreationPolicy.SINGLETON)) {
            throw new RuntimeException("GameBean called " + name + " for class " + beanClass.getName() + " not found!");
        }
        if (b.getCreationPolicy().equals(GameBeanCreationPolicy.OBJECT)) {
            exists = ComponentCreator.create(beanClass);
            b.setBean(exists);
            beans.get(beanClass).put(name, b);
        }
        return exists;
    }

    private void scanPackages(String[] packages, PackageLoader loader) {
        scanFrom = this.getClass();
        for (String p : packages) {
            scanPackage(p, loader);
        }
    }

    public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config, PackageLoader loader) {
        if(config.isAnnotationPresent(Components.class)) {
            scanFrom = config;
            Components components = config.getAnnotation(Components.class);
            loadBeansFrom(components.value());
        }
        if (config.isAnnotationPresent(ComponentScan.class)) {
            scanFrom = config;
            ComponentScan componentScan = config.getAnnotation(ComponentScan.class);
            scanPackages(componentScan.value(), loader);
        }
        loadBeanFrom(config);
    }

    private void loadBeansFrom(Class<?>[] classes) {
        for(Class<?> c : classes) {
            loadBeanFrom(c);
        }
    }

    public <C extends AbstractConfiguration> void loadConfiguration(Class<C> config) {
        loadConfiguration(config, this::findAllClassesUsingClassLoader);
    }

    private ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = scanFrom.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }
}
