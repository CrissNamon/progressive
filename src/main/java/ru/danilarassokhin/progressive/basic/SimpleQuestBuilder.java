package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.builders.QuestBuilder;
import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;

public class SimpleQuestBuilder implements QuestBuilder<SimpleStoryQuest> {

    private SimpleStoryQuest quest;

    public SimpleQuestBuilder(Long id) {
        quest = new SimpleStoryQuest(id);
    }

    @Override
    public SimpleStoryQuest build() {
        return quest;
    }

    @Override
    public SimpleQuestBuilder setName(String name) {
        quest.setName(name);
        return this;
    }

    @Override
    public SimpleQuestBuilder setCompleteCondition(StoryCondition completeCondition) {
        quest.setCompleteCondition(completeCondition);
        return this;
    }

    @Override
    public SimpleQuestBuilder setUnique(boolean unique) {
        quest.setUnique(unique);
        return this;
    }

    @Override
    public SimpleQuestBuilder setOnComplete(StoryActionObject<SimpleStoryQuest> onComplete) {
        quest.setOnComplete(onComplete);
        return this;
    }

}
