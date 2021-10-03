package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.component.AbstractGameComponent;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class BasicGameObject extends AbstractGameComponent implements GameObject {

    private final Map<Class<? extends GameScript>, GameScript> scripts;
    private final Set<GameScriptWorker> scriptWorkers;
    private Long scriptIdGenerator;

    protected BasicGameObject(Long id) {
        super(id);
        scripts = new HashMap<>();
        scriptWorkers = new HashSet<>();
        scriptIdGenerator = 0L;
    }

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
            Constructor gameScriptConstructor = null;
            try {
                gameScriptConstructor = gameScriptClass.getDeclaredConstructor();
            }catch (NoSuchMethodException e) {
                throw new RuntimeException("GameScript " + gameScriptClass.getName() + " must have an empty constructor!");
            }
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            gameScript = ComponentCreator.create(gameScriptClass);
            Method setGameObject = gameScript.getClass().getDeclaredMethod("setGameObject", GameObject.class);
            setGameObject.setAccessible(true);
            lookup.unreflect(setGameObject).invoke(gameScript, this);
            gameScript.wireFields();
            Method update = gameScript.getClass().getDeclaredMethod("update");
            Method start = gameScript.getClass().getDeclaredMethod("start");
            if(!ComponentCreator.isModifierSet(update.getModifiers(), Modifier.PRIVATE)
                    || !ComponentCreator.isModifierSet(start.getModifiers(), Modifier.PRIVATE)) {
                throw new RuntimeException("GameScript must have private void start() and private void update() methods!");
            }
            update.setAccessible(true);
            start.setAccessible(true);
            scriptWorkers.add(new GameScriptWorker(lookup.unreflect(update), lookup.unreflect(start), gameScriptClass, ++scriptIdGenerator));
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
                    + getClass() + "), methods private void start() and private void update() methods! Exception: " + e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("GameScript setGameObject invokation failed: " + throwable.getMessage());
        }
    }

    @Override
    public <V extends GameScript> boolean hasGameScript(Class<V> gameScriptClass) {
        return scripts.containsKey(gameScriptClass);
    }

    private void start() {
        switch (BasicGame.getInstance().getGameTickRateType()) {
            case PARALLEL:
                scriptWorkers.parallelStream().forEach(this::callStartInGameScript);
                break;
            case SEQUENCE:
                for(GameScriptWorker worker : scriptWorkers) {
                    callStartInGameScript(worker);
                }
                break;
        }
    }

    private void update() {
        switch (BasicGame.getInstance().getGameTickRateType()) {
            case PARALLEL:
                scriptWorkers.parallelStream().forEach(this::callUpdateInGameScript);
                break;
            case SEQUENCE:
                for(GameScriptWorker worker : scriptWorkers) {
                    callUpdateInGameScript(worker);
                }
        }
    }

    private void callStartInGameScript(GameScriptWorker worker) {
        try {
            worker.getStartMethod()
                    .invoke(
                            scripts.get(worker.getScriptClass())
                    );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("Error occurred during calling start method on GameObject "
                    + worker.getScriptClass().getName());
        }
    }

    private void callUpdateInGameScript(GameScriptWorker worker) {
        try {
            worker.getUpdateMethod()
                    .invoke(
                            scripts.get(worker.getScriptClass())
                    );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("Error occurred during calling start method on GameObject "
                    + worker.getScriptClass());
        }
    }

}
