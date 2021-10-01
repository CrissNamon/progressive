package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.RequiredScript;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.GameAnnotation;
import ru.danilarassokhin.progressive.util.GameAnnotationProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class BasicGameObject implements GameObject {

    private Long id;

    private final Map<Class<? extends GameScript>, GameScript> scripts;

    protected BasicGameObject(Long id) {
        this.id = id;
        scripts = new HashMap<>();
    }

    @Override
    public <V extends GameScript> V getGameScript(Class<V> gameScriptClass) {
        if(!GameAnnotationProcessor.isAnnotationPresent(GameAnnotation.IS_GAME_SCRIPT, gameScriptClass)) {
            throw new RuntimeException(gameScriptClass.getName() + " has no @Script annotation. All GameScripts must be annotated with @Script!");
        }
        BasicObjectCaster objectCaster = new BasicObjectCaster();
        GameScript script = scripts.getOrDefault(gameScriptClass, null);
        if(script != null) {
            return objectCaster.cast(script, gameScriptClass, (o) -> {});
        }
        try {
            script = gameScriptClass.getDeclaredConstructor().newInstance();
            script.setParent(this);
            if(gameScriptClass.isAnnotationPresent(RequiredScript.class)) {
                RequiredScript requiredScripts = gameScriptClass.getAnnotation(RequiredScript.class);
                for(Class<? extends GameScript> req : requiredScripts.value()) {
                    getGameScript(req);
                }
            }
            script.wireFields();
            if(scripts.putIfAbsent(gameScriptClass, script) != null) {
                throw new RuntimeException("Could not register GameScript " + gameScriptClass.getName() + "! GameScript already exists");
            }
            return objectCaster.cast(script, gameScriptClass, (o) -> {});
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Script creation failure! GameScript must have public empty constructor! Exception: " + e.getMessage());
        }
    }

    @Override
    public Long getId() {
        return id;
    }
}
