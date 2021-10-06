package ru.danilarassokhin.progressive.basic.util;

import ru.danilarassokhin.progressive.annotation.GameBean;
import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.util.GameObjectCaster;

/**
 * Basic implementation of object caster
 */
@GameBean(name = "objCaster")
public final class BasicObjectCaster implements GameObjectCaster {

    @Override
    public <T, O> T cast(O from, Class<T> to, GameActionObject<T> onSuccessCast) {
        try {
            T casted = to.cast(from);
            onSuccessCast.make(casted);
            return casted;
        }catch (ClassCastException e) {
            return null;
        }
    }
}
