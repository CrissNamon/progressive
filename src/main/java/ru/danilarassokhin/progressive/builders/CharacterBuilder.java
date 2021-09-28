package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.data.StoryCharacter;

public interface CharacterBuilder<V extends StoryCharacter> extends Builder<V>{

    CharacterBuilder setHealth(float health);
    CharacterBuilder setName(String name);

}
