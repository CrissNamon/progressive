package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.component.AbstractGameComponent;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.util.HashMap;
import java.util.Map;

public final class BasicGameObject extends AbstractGameComponent implements GameObject {

    private final Map<Class<? extends GameScript>, GameScript> scripts;

    protected BasicGameObject(Long id) {
        super(id);
        scripts = new HashMap<>();
    }

    @Override
    public <V extends GameScript> V getGameScript(Class<V> gameScriptClass) {
        if(!ComponentAnnotationProcessor.isAnnotationPresent(IsGameScript.class, gameScriptClass)) {
            throw new RuntimeException(gameScriptClass.getName() + " has no @IsGameScript annotation. All GameScripts must be annotated with @IsGameScript!");
        }
        BasicObjectCaster objectCaster = new BasicObjectCaster();
        GameScript gameScript = scripts.getOrDefault(gameScriptClass, null);
        if(gameScript != null) {
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        }
        try {
            gameScript = ComponentCreator.create(gameScriptClass);
            gameScript.setParent(this);
            if(gameScriptClass.isAnnotationPresent(RequiredGameScript.class)) {
                RequiredGameScript requiredGameScripts = gameScriptClass.getAnnotation(RequiredGameScript.class);
                for (Class<? extends GameScript> req : requiredGameScripts.value()) {
                    if(!requiredGameScripts.lazy()) {
                        getGameScript(req);
                    }else {
                       if(!hasGameScript(req)) {
                           throw new RuntimeException(gameScriptClass.getName() + " requires " + req.getName() + " which is not attached to " + this);
                       }
                    }
                }
            }
            gameScript.wireFields();
            if(scripts.putIfAbsent(gameScriptClass, gameScript) != null) {
                throw new RuntimeException("Could not register IsGameScript " + gameScriptClass.getName() + "! IsGameScript already exists");
            }
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IsGameScript creation failure! IsGameScript must have public empty constructor! Exception: " + e.getMessage());
        }
    }

    @Override
    public <V extends GameScript> boolean hasGameScript(Class<V> gameScriptClass) {
        return scripts.containsKey(gameScriptClass);
    }
}
