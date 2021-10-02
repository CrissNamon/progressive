package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.annotation.Autofill;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;

public class MyClass {

    @Autofill
    private BasicObjectCaster basicObjectCaster;

    public BasicObjectCaster getBasicObjectCaster() {
        return basicObjectCaster;
    }
}
