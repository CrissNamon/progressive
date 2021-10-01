package ru.danilarassokhin.progressive.annotation;

import java.lang.annotation.*;

@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FromParent {
}
