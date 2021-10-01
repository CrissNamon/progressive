package ru.danilarassokhin.progressive.component;

public interface GameObject extends GameComponent{

    <V extends GameScript> V getGameScript(Class<V> gameScriptClass);

}
