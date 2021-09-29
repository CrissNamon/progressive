package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.annotations.StorySystemRequirement;
import ru.danilarassokhin.progressive.system.StorySystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@StorySystemRequirement({QuestSystem.class, LocationSystem.class, ItemSystem.class})
public class CharacterSystem implements StorySystem<Long, SimpleStoryCharacter> {

    private final Map<Long, SimpleStoryCharacter> characters;

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
        Class<?> c = this.getClass();
        Set<Class<? extends StorySystem>> requirements = new HashSet<>();
        if(c.isAnnotationPresent(StorySystemRequirement.class)) {
            Class<? extends StorySystem>[] req = c.getAnnotation(StorySystemRequirement.class).value();
            for(int i = 0; i < req.length; ++i) {
                requirements.add(req[i]);
            }
        }
        return requirements;
    }

    @Override
    public SimpleStoryCharacter getComponentById(Long id) {
        return characters.getOrDefault(id, null);
    }
}
