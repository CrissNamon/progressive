package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.component.AbstractGameComponent;
import ru.danilarassokhin.progressive.basic.system.AbstractGameScript;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class BasicGameObject extends AbstractGameComponent implements GameObject {

    private final Map<Long, GameScript> scripts;
    private final Set<GameObjectWorker> scriptWorkers;
    private Long scriptIdGenerator;

    protected BasicGameObject(Long id) {
        super(id);
        scripts = new HashMap<>();
        scriptWorkers = new HashSet<>();
        scriptIdGenerator = 0L;
    }

    @Override
    public <V extends AbstractGameScript> V getGameScript(Class<V> gameScriptClass) {
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
                        getGameScript((Class<? extends AbstractGameScript>) req);
                    }else {
                        if(!hasGameScript((Class<? extends AbstractGameScript>)req)) {
                            throw new RuntimeException(gameScriptClass.getName() + " requires " + req.getName() + " which is not attached to " + this);
                        }
                    }
                }
            }
            Constructor gameScriptConstructor = gameScriptClass.getDeclaredConstructor(getClass());
            if(!ComponentCreator.isModifierSet(gameScriptConstructor.getModifiers(), Modifier.PRIVATE)) {
                throw new RuntimeException("GameScript " + gameScriptClass.getName() + " constructor must be private!");
            }
            gameScript = ComponentCreator.create(gameScriptClass, this);
            gameScript.wireFields();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Method update = gameScript.getClass().getDeclaredMethod("update");
            Method start = gameScript.getClass().getDeclaredMethod("start");
            if(!ComponentCreator.isModifierSet(update.getModifiers(), Modifier.PRIVATE)
                    || !ComponentCreator.isModifierSet(start.getModifiers(), Modifier.PRIVATE)) {
                throw new RuntimeException("GameScript must have private void start() and private void update() methods!");
            }
            update.setAccessible(true);
            start.setAccessible(true);
            scriptWorkers.add(new GameObjectWorker(lookup.unreflect(update), lookup.unreflect(start), ++scriptIdGenerator));
            if(scripts.putIfAbsent(scriptIdGenerator, gameScript) != null) {
                throw new RuntimeException("Could not register IsGameScript " + gameScriptClass.getName() + "! IsGameScript already exists");
            }
            return objectCaster.cast(gameScript, gameScriptClass, (o) -> {});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IsGameScript creation failure! Exception: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("GameScript must have private constructor " + gameScript.getClass() + "("
                    + getClass() + "), methods private void start() and private void update() methods! Exception: " + e.getMessage());
        }
    }

    @Override
    public <V extends AbstractGameScript> boolean hasGameScript(Class<V> gameScriptClass) {
        return scripts.containsKey(gameScriptClass);
    }

    private void start() {
        switch (BasicGame.getInstance().getGameTickRateType()) {
            case PARALLEL:
                scriptWorkers.parallelStream().forEach(this::callStartInGameScript);
                break;
            case SEQUENCE:
                for(GameObjectWorker worker : scriptWorkers) {
                    callStartInGameScript(worker);
                }
        }
    }

    private void update() {
        switch (BasicGame.getInstance().getGameTickRateType()) {
            case PARALLEL:
                scriptWorkers.parallelStream().forEach(this::callUpdateInGameScript);
                break;
            case SEQUENCE:
                for(GameObjectWorker worker : scriptWorkers) {
                    callUpdateInGameScript(worker);
                }
        }
    }

    private void callStartInGameScript(GameObjectWorker worker) {
        try {
            worker.getStartMethod()
                    .invoke(
                            scripts.get(worker.getGameObjId())
                    );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("Error occurred during calling start method on GameObject " + worker.getGameObjId());
        }
    }

    private void callUpdateInGameScript(GameObjectWorker worker) {
        try {
            worker.getUpdateMethod()
                    .invoke(
                            scripts.get(worker.getGameObjId())
                    );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("Error occurred during calling start method on GameObject " + worker.getGameObjId());
        }
    }

}
