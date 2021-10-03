package ru.danilarassokhin.progressive.component;

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

}
