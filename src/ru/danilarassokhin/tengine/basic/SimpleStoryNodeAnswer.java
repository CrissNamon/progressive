package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryExtraAction;
import ru.danilarassokhin.tengine.StoryNodeAnswer;

public class SimpleStoryNodeAnswer implements StoryNodeAnswer<String> {

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
