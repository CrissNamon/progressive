package ru.danilarassokhin.progressive.util;

import ru.danilarassokhin.progressive.annotation.Script;

import java.lang.annotation.Annotation;

public interface GameAnnotationProcessor {

    static boolean isAnnotationPresent(GameAnnotation checker, Class<?> c) {
        return findAnnotation(c, checker.getAnnotation()) != null;
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        Annotation[] anns = clazz.getDeclaredAnnotations();
        for (Annotation ann : anns) {
            if (ann.annotationType() == annotationType) {
                return (A) ann;
            }
        }
        for (Annotation ann : anns) {
            A annotation = findAnnotation(ann.annotationType(), annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            A annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || Object.class == superclass) {
            return null;
        }
        return findAnnotation(superclass, annotationType);
    }

}
