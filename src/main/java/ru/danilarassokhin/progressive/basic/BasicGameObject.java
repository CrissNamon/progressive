package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.annotation.isGameScript;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.GameAnnotationProcessor;
import ru.danilarassokhin.progressive.util.GameComponentInstantiator;

import java.util.HashMap;
import java.util.Map;

public final class BasicGameObject implements GameObject {

    private Long id;

    private final Map<Class<? extends ru.danilarassokhin.progressive.component.GameScript>, ru.danilarassokhin.progressive.component.GameScript> scripts;

    protected BasicGameObject(Long id) {
        this.id = id;
        scripts = new HashMap<>();
    }

    @Override
    public <V extends GameScript> V getGameScript(Class<V> gameScriptClass) {
        if(!GameAnnotationProcessor.isAnnotationPresent(isGameScript.class, gameScriptClass)) {
            throw new RuntimeException(gameScriptClass.getName() + " has no @isGameScript annotation. All GameScripts must be annotated with @isGameScript!");
        }
        BasicObjectCaster objectCaster = new BasicObjectCaster();
        GameScript gameScript = scripts.getOrDefault(gameScriptClass, null);
        if(gameScript != null) {
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        }
        try {
            gameScript = GameComponentInstantiator.instantiate(gameScriptClass);
            gameScript.setParent(this);
            if(gameScriptClass.isAnnotationPresent(RequiredGameScript.class)) {
                RequiredGameScript requiredGameScripts = gameScriptClass.getAnnotation(RequiredGameScript.class);
                for(Class<? extends GameScript> req : requiredGameScripts.value()) {
                    getGameScript(req);
                }
            }
            gameScript.wireFields();
            if(scripts.putIfAbsent(gameScriptClass, gameScript) != null) {
                throw new RuntimeException("Could not register isGameScript " + gameScriptClass.getName() + "! isGameScript already exists");
            }
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("isGameScript creation failure! isGameScript must have public empty constructor! Exception: " + e.getMessage());
        }
    }

    @Override
    public Long getId() {
        return id;
    }
}
