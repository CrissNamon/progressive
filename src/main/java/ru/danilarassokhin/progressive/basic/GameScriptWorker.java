package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.component.GameScript;

import java.lang.invoke.MethodHandle;

public class GameScriptWorker {

    private final MethodHandle updateMethod;
    private final MethodHandle startMethod;
    private final Class<? extends GameScript> scriptClass;
    private final Long id;

    public GameScriptWorker(MethodHandle updateMethod, MethodHandle startMethod, Class<? extends GameScript> gameScriptClass, Long id) {
        this.updateMethod = updateMethod;
        this.startMethod = startMethod;
        this.scriptClass = gameScriptClass;
        this.id = id;
    }

    public MethodHandle getUpdateMethod() {
        return updateMethod;
    }

    public MethodHandle getStartMethod() {
        return startMethod;
    }

    public Class<? extends GameScript> getScriptClass() {
        return scriptClass;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

}
