package ru.danilarassokhin.progressive.basic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.annotation.RequiredGameScript;
import ru.danilarassokhin.progressive.basic.util.BasicComponentCreator;
import ru.danilarassokhin.progressive.basic.util.BasicObjectCaster;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.component.GameScript;
import ru.danilarassokhin.progressive.exception.GameScriptException;
import ru.danilarassokhin.progressive.util.ComponentAnnotationProcessor;

/**
 * Basic game object contains all necessary logic for simple objects
 * <p color="orange">This class is not extendable. Create game object from zero if you need to</red>
 */
public final class BasicGameObject implements GameObject {

  private final Long id;

  private final Map<Class<? extends GameScript>, GameScript> scripts;

  protected BasicGameObject(Long id) {
    this.id = id;
    scripts = new ConcurrentHashMap<>();
  }

  @Override
  public Set<GameScript> getGameScripts() {
    return new HashSet<>(scripts.values());
  }

  @Override
  public <V extends GameScript> V getGameScript(Class<V> gameScriptClass, Object... args) {
    if (!ComponentAnnotationProcessor.isAnnotationPresent(IsGameScript.class, gameScriptClass)) {
      throw new GameScriptException(
          gameScriptClass.getName()
          + " has no @IsGameScript annotation. "
          + "All GameScripts must be annotated with @IsGameScript!");
    }
    BasicObjectCaster objectCaster = new BasicObjectCaster();
    GameScript gameScript = scripts.getOrDefault(gameScriptClass, null);
    if (gameScript != null) {
      return objectCaster.cast(gameScript, gameScriptClass);
    }
    try {
      if (gameScriptClass.isAnnotationPresent(RequiredGameScript.class)) {
        RequiredGameScript requiredGameScripts =
            gameScriptClass.getAnnotation(RequiredGameScript.class);
        for (Class<? extends GameScript> req : requiredGameScripts.value()) {
          if (!requiredGameScripts.lazy()) {
            getGameScript(req);
          } else if (!hasGameScript(req)){
            throw new GameScriptException(gameScriptClass.getName() + " requires "
                + req.getName() + " which is not attached to " + this);
          }
        }
      }
      gameScript = BasicComponentCreator.create(gameScriptClass, args);
      gameScript.setGameObject(this);
      gameScript.wireFields();
      if (scripts.putIfAbsent(gameScriptClass, gameScript) != null) {
        throw new GameScriptException("Could not register IsGameScript "
            + gameScriptClass.getName() + "! IsGameScript already exists");
      }
      return objectCaster.cast(gameScript, gameScriptClass);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      throw new GameScriptException("@IsGameScript "
          + gameScriptClass.getName()
          + " creation failure! Exception: "
          + e.getMessage());
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

  @Override
  public <V extends GameScript> boolean removeGameScript(Class<V> scriptClass) {
    return scripts.remove(scriptClass) != null;
  }

  @Override
  public synchronized void dispose() {
    scripts.values().stream().parallel().unordered()
        .forEach(GameScript::dispose);
    scripts.clear();
  }

  @Override
  public void update(long delta) {
    scripts.values()
        .stream().parallel().unordered()
        .forEach(s -> s.update(delta));
  }

  @Override
  public void start() {
    scripts.values()
        .stream().parallel().unordered()
        .forEach(GameScript::start);
  }
}
