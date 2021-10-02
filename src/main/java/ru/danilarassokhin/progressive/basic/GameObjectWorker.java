package ru.danilarassokhin.progressive.basic;

import java.lang.reflect.Method;

public final class GameObjectWorker {

    private final Method updateMethod;
    private final Method startMethod;
    private final Long gameObjId;

    public GameObjectWorker(Method updateMethod, Method startMethod, Long gameObjId) {
        this.updateMethod = updateMethod;
        this.startMethod = startMethod;
        this.gameObjId = gameObjId;
    }

    public Method getUpdateMethod() {
        return updateMethod;
    }

    public Method getStartMethod() {
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
