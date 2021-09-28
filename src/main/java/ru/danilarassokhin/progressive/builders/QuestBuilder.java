package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.data.StoryQuest;

public interface QuestBuilder<V extends StoryQuest> extends Builder<V> {

    QuestBuilder setName(String name);
    QuestBuilder setCompleteCondition(StoryCondition completeCondition);
    QuestBuilder setUnique(boolean unique);
    QuestBuilder setOnComplete(StoryActionObject<V> onComplete);

}
