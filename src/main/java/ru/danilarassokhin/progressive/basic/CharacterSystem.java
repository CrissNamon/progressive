package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.component.StorySystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CharacterSystem implements StorySystem<Long, SimpleStoryCharacter> {

    private final Map<Long, SimpleStoryCharacter> characters;
    private final Set<Class<? extends StorySystem>> requirements = Set.of(LocationSystem.class, QuestSystem.class, ItemSystem.class);

    protected CharacterSystem() {
        characters = new HashMap<>();
    }

    @Override
    public Map<Long, SimpleStoryCharacter> getComponents() {
        return characters;
    }

    @Override
    public SimpleStoryCharacter addComponent(Long id) {
        SimpleStoryCharacter character = new SimpleStoryCharacter(id);
        if(characters.putIfAbsent(id, character) == null) {
            return character;
        }else{
            return null;
        }
    }

    @Override
    public void removeComponent(Long id) {
        characters.remove(id);
    }

    @Override
    public Set<Class<? extends StorySystem>> getRequirements() {
        return requirements;
    }

    @Override
    public SimpleStoryCharacter getComponentById(Long id) {
        return characters.getOrDefault(id, null);
    }
}
