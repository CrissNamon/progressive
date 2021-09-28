package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryAction;
import ru.danilarassokhin.progressive.data.StoryNodeAnswer;

import java.io.Serializable;

public class SimpleStoryNodeAnswer implements StoryNodeAnswer<String>, Serializable {

    private String text;
    private StoryAction onAnswer;

    public SimpleStoryNodeAnswer(String text, StoryAction onAnswer) {
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
