package ru.danilarassokhin.progressive.basic;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

public final class GameObjectWorker {

    private final MethodHandle updateMethod;
    private final MethodHandle startMethod;
    private final Long gameObjId;

    public GameObjectWorker(MethodHandle updateMethod, MethodHandle startMethod, Long gameObjId) {
        this.updateMethod = updateMethod;
        this.startMethod = startMethod;
        this.gameObjId = gameObjId;
    }

    public MethodHandle getUpdateMethod() {
        return updateMethod;
    }

    public MethodHandle getStartMethod() {
        return startMethod;
    }

    public Long getGameObjId() {
        return gameObjId;
    }

    @Override
    public int hashCode() {
        return gameObjId.intValue();
    }
}
