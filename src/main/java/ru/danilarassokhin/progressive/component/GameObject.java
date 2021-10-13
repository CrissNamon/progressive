package ru.danilarassokhin.progressive.component;

import java.util.Collection;

/**
 * Represents abstract GameObject
 */
public interface GameObject extends GameComponent{

    /**
     * Returns existed GameScript attached to this object or creates new
     * @param gameScriptClass GameScript class to get
     * @param <V> GameScript to return
     * @return Game script
     */
    <V extends GameScript> V getGameScript(Class<V> gameScriptClass);

    /**
     * Checks if this object has given GameScript attached to this object
     * @param gameScriptClass GameScript class to get
     * @param <V> GameScript type
     * @return true if given script is attached
     */
    <V extends GameScript> boolean hasGameScript(Class<V> gameScriptClass);

    /**
     * Removes game script from this object
     * @param gameScriptClass GameScript class to remove
     * @param <V> GameScript type
     * @return true if script has been removed
     */
    <V extends GameScript> boolean removeGameScript(Class<V> gameScriptClass);

    /**
     * Gets all scripts attached to this object
     * @return Collection of GameScripts
     */
    Collection<GameScript> getGameScripts();

    void dispose();

}
