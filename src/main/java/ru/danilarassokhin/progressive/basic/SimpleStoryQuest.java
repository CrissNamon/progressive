package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.StoryCondition;
import ru.danilarassokhin.progressive.StoryObjectExtraAction;
import ru.danilarassokhin.progressive.StoryQuest;

import java.io.Serializable;

public class SimpleStoryQuest implements StoryQuest<Long>, Serializable, AutoCloseable {

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
        return isCompleted;
    }

    @Override
    public boolean complete() {
        if(completeCondition.isTrue() && !isCompleted) {
            onComplete.make(this);
            isCompleted = true;
        }
        return isCompleted;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void close() throws ClassCastException {

    }
}
