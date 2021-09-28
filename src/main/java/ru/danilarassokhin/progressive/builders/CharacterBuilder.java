package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.data.StoryCharacter;

/**
 * Represents character builder
 * @param <V> Character type
 */
public interface CharacterBuilder<V extends StoryCharacter> extends Builder<V>{

    /**
     * Sets start health
     * @param health Start health
     * @return Character builder
     */
    CharacterBuilder setHealth(float health);

    /**
     * Sets character name
     * @param name Name to set
     * @return Character builder
     */
    CharacterBuilder setName(String name);

}
