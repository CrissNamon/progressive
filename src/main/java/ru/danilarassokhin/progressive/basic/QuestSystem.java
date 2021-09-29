package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.system.StorySystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QuestSystem implements StorySystem<Long, SimpleStoryQuest> {

    private final Map<Long, SimpleStoryQuest> quests;

    protected  QuestSystem() {
        quests = new HashMap<>();
    }

    @Override
    public Map<Long, SimpleStoryQuest> getComponents() {
        return quests;
    }

    @Override
    public SimpleStoryQuest addComponent(Long id) {
        SimpleStoryQuest quest = new SimpleStoryQuest(id);
        if(quests.putIfAbsent(id, quest) == null) {
            return quest;
        }else{
            return null;
        }
    }

    @Override
    public void removeComponent(Long id) {
        quests.remove(id);
    }

    @Override
    public Set<Class<? extends StorySystem>> getRequirements() {
        return null;
    }

    @Override
    public SimpleStoryQuest getComponentById(Long id) {
        return quests.getOrDefault(id, null);
    }
}
