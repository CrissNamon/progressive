package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.builders.CharacterBuilder;

public class SimpleCharacterBuilder implements CharacterBuilder<SimpleStoryCharacter> {

    private final SimpleStoryCharacter character;

    public SimpleCharacterBuilder(Long id) {
        character = new SimpleStoryCharacter(id);
    }

    @Override
    public SimpleCharacterBuilder setHealth(float health) {
        character.setHealth(health);
        return this;
    }

    @Override
    public SimpleCharacterBuilder setName(String name) {
        character.setName(name);
        return this;
    }

    public SimpleCharacterBuilder setLocation(SimpleStoryLocation location) {
        character.setLocation(location);
        return this;
    }

    @Override
    public SimpleStoryCharacter build() {
        return character;
    }
}
