package ru.danilarassokhin.progressive.injection;

import java.util.Set;

public interface PackageLoader {
    Set<Class> findAllClassesIn(String packageName);
}
