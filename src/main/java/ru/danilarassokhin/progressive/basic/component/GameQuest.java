package ru.danilarassokhin.progressive.basic.component;

import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.lambda.GameCondition;

public class GameQuest extends AbstractGameComponent {

    private String name;
    private GameCondition completeCondition;
    private GameActionObject<GameQuest> onComplete;
    private boolean isCompleted;
    private boolean unique;

    public GameQuest(Long id){
        super(id);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean complete() {
        if(completeCondition.isTrue() && !isCompleted) {
            onComplete.make(this);
            isCompleted = true;
        }
        return isCompleted;
    }

    public void setOnComplete(GameActionObject<GameQuest> onComplete) {
        this.onComplete = onComplete;
    }

    public void setCompleteCondition(GameCondition completeCondition) {
        this.completeCondition = completeCondition;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
    
    public void close() throws ClassCastException {

    }

    public int hashCode() {
        return getId().intValue();
    }
}
