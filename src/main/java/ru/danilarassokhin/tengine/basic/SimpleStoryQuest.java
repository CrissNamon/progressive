package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryCondition;
import ru.danilarassokhin.tengine.StoryObjectExtraAction;
import ru.danilarassokhin.tengine.StoryQuest;

import java.io.Serializable;

public class SimpleStoryQuest implements StoryQuest<Long>, Serializable {

    private final Long id;
    private String name;
    private StoryCondition completeCondition;
    private StoryObjectExtraAction<SimpleStoryQuest> onComplete;
    private boolean isCompleted;
    private boolean unique;

    protected SimpleStoryQuest(Long id, String name, StoryCondition completeCondition, StoryObjectExtraAction<SimpleStoryQuest> onComplete) {
        this.id = id;
        this.name = name;
        this.completeCondition = completeCondition;
        this.onComplete = onComplete;
        this.isCompleted = false;
        this.unique = true;
    }

    protected SimpleStoryQuest(Long id, String name, boolean unique, StoryCondition completeCondition, StoryObjectExtraAction<SimpleStoryQuest> onComplete) {
        this.id = id;
        this.name = name;
        this.completeCondition = completeCondition;
        this.onComplete = onComplete;
        this.isCompleted = false;
        this.unique = unique;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompleted() {
        return completeCondition.isTrue();
    }

    @Override
    public boolean complete() {
        isCompleted = isCompleted();
        if(isCompleted) {
            onComplete.make(this);
        }
        return isCompleted;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }
}
