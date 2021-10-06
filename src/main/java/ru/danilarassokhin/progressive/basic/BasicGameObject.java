package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic game object contains all necessary logic for simple objects
 * <p color="orange">This class is not extendable. Create game object from zero if you need to</red>
 */
public final class BasicGameObject implements GameObject {

    private Long id;

    private final Map<Class<? extends GameScript>, GameScript> scripts;

    protected BasicGameObject(Long id) {
        this.id = id;
        scripts = new HashMap<>();
    }

    /**
     * Returns all attached scripts
     * @return All scripts attached to this object
     */
    public Collection<GameScript> scripts() {
        return scripts.values();
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
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            gameScript = ComponentCreator.create(gameScriptClass);
            Method setGameObject = gameScript.getClass().getDeclaredMethod("setGameObject", GameObject.class);
            setGameObject.setAccessible(true);
            lookup.unreflect(setGameObject).invoke(gameScript, this);
            gameScript.wireFields();
            if(scripts.putIfAbsent(gameScriptClass, gameScript) != null) {
                throw new RuntimeException("Could not register IsGameScript " + gameScriptClass.getName() + "! IsGameScript already exists");
            }
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IsGameScript creation failure! Exception: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("GameScript must have empty constructor " + gameScript.getClass() + "("
                    + getClass() + ") and setGameObject(GameObject) method! Exception: " + e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("GameScript setGameObject invokation failed: " + throwable.getMessage());
        }
    }

    @Override
    public <V extends GameScript> boolean hasGameScript(Class<V> gameScriptClass) {
        return scripts.containsKey(gameScriptClass);
    }

    @Override
    public Long getId() {
        return id;
    }

    public <V extends GameScript> boolean removeScript(Class<V> scriptClass) {
        if(!scripts.containsKey(scriptClass)) {
            return false;
        }
        GameScript script = scripts.get(scriptClass);
        scripts.remove(scriptClass);
        script = null;
        return true;
    }

    @Override
    public void dispose() {
        for(Class script : scripts.keySet()) {
            removeScript(script);
        }
    }
}
