package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.StoryExtraAction;
import ru.danilarassokhin.progressive.StoryNodeAnswer;

import java.io.Serializable;

public class SimpleStoryNodeAnswer implements StoryNodeAnswer<String>, Serializable {

    private String text;
    private StoryExtraAction onAnswer;

    public SimpleStoryNodeAnswer(String text, StoryExtraAction onAnswer) {
        this.text = text;
        this.onAnswer = onAnswer;
    }

    @Override
    public String getContent() {
        return text;
    }

    @Override
    public void onAnswer() {
        if(onAnswer != null) {
            onAnswer.make();
        }
    }

}
