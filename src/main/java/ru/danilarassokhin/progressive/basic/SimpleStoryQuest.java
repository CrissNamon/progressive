package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.lambdas.StoryActionObject;
import ru.danilarassokhin.progressive.data.StoryQuest;

import java.io.Serializable;

public class SimpleStoryQuest implements StoryQuest<Long>, Serializable, AutoCloseable {

    private final Long id;
    private String name;
    private StoryCondition completeCondition;
    private StoryActionObject<SimpleStoryQuest> onComplete;
    private boolean isCompleted;
    private boolean unique;

    protected SimpleStoryQuest(Long id, String name, StoryCondition completeCondition, StoryActionObject<SimpleStoryQuest> onComplete) {
        this.id = id;
        this.name = name;
        this.completeCondition = completeCondition;
        this.onComplete = onComplete;
        this.isCompleted = false;
        this.unique = true;
    }

    protected SimpleStoryQuest(Long id, String name, boolean unique, StoryCondition completeCondition, StoryActionObject<SimpleStoryQuest> onComplete) {
        this.id = id;
        this.name = name;
        this.completeCondition = completeCondition;
        this.onComplete = onComplete;
        this.isCompleted = false;
        this.unique = unique;
    }

    protected SimpleStoryQuest(Long id) {
        this.id = id;
        this.name = "";
        this.completeCondition = () -> false;
        this.onComplete = (q) -> {};
        this.isCompleted = false;
        this.unique = true;
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
    public void setName(String name) {
        this.name = name;
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
    public void setOnComplete(StoryActionObject onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public void setCompleteCondition(StoryCondition completeCondition) {
        this.completeCondition = completeCondition;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public void close() throws ClassCastException {

    }
}
