package ru.danilarassokhin.progressive.util;

import java.lang.annotation.*;

public interface GameAnnotationProcessor {

    static boolean isAnnotationPresent(Class<? extends Annotation> an, Class<?> c) {
        return findAnnotation(c, an) != null;
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if(clazz.equals(Target.class) || clazz.equals(Documented.class) || clazz.equals(Retention.class)
                || clazz.equals(Inherited.class) || clazz.equals(Deprecated.class)) {
            return null;
        }
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
