package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryCondition;
import ru.danilarassokhin.progressive.data.StoryLocation;

import java.io.Serializable;

public class SimpleStoryLocation implements StoryLocation<Long>, Serializable, AutoCloseable {

    private final Long id;
    private transient String name;
    private transient StoryCondition entryRestriction;

    protected SimpleStoryLocation(Long id, String name) {
        this.id = id;
        this.name = name;
        this.entryRestriction = () -> true;
    }

    protected SimpleStoryLocation(Long id, String name, StoryCondition entryRestriction) {
        this.id = id;
        this.name = name;
        this.entryRestriction = entryRestriction;
    }

    protected SimpleStoryLocation(Long id) {
        this.id = id;
        this.name = "";
        this.entryRestriction = () -> true;
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

    public void setEntryRestriction(StoryCondition entryRestriction) {
        this.entryRestriction = entryRestriction;
    }

    @Override
    public boolean canEntry() {
        if(entryRestriction != null) {
            return entryRestriction.isTrue();
        }else{
            return true;
        }
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }

    @Override
    public void close() throws ClassCastException {

    }
}
