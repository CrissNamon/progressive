package ru.danilarassokhin.progressive.builders;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.component.StoryQuest;

/**
 * Represents builder for quest
 * @param <V> Quest type
 */
public interface QuestBuilder<V extends StoryQuest> extends Builder<V> {

    /**
     * Sets quest name
     * @param name Quest name
     * @return Quest builder
     */
    QuestBuilder setName(String name);

    /**
     * Sets complete condition
     * @param completeCondition Condition
     * @return Quest builder
     */
    QuestBuilder setCompleteCondition(StoryCondition completeCondition);

    /**
     * Sets quest uniqueness
     * @param unique If quest is unique (one-time)
     * @return Quest builder
     */
    QuestBuilder setUnique(boolean unique);

    /**
     * Sets action on quest complete
     * @param onComplete On complete action
     * @return Quest builder
     */
    QuestBuilder setOnComplete(StoryActionObject<V> onComplete);

}
