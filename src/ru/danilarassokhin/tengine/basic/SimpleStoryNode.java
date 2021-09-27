package ru.danilarassokhin.tengine.basic;

import ru.danilarassokhin.tengine.StoryNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleStoryNode implements StoryNode<Long, String, SimpleStoryNodeAnswer> {

    private final Long id;
    private final String text;
    private final List<SimpleStoryNodeAnswer> answers;

    public SimpleStoryNode(Long id, String text, List<SimpleStoryNodeAnswer> answers) {
        this.id = id;
        this.text = text;
        this.answers = answers;
    }

    public SimpleStoryNode(Long id, String text, SimpleStoryNodeAnswer... answers) {
        this.id = id;
        this.text = text;
        this.answers = new ArrayList<>();
        addAnswer(answers);
    }

    public SimpleStoryNode(Long id, String text) {
        this.id = id;
        this.text = text;
        this.answers = new ArrayList<>();
    }

    @Override
    public String getContent() {
        return text;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<SimpleStoryNodeAnswer> getAnswers() {
        return answers;
    }

    @Override
    public void addAnswer(SimpleStoryNodeAnswer... answer) {
        answers.addAll(Arrays.asList(answer));
    }

    @Override
    public boolean hasAnswers() {
        return !answers.isEmpty();
    }

    @Override
    public SimpleStoryNodeAnswer answer(int i) {
        if(i >= 0 && i < answers.size()) {
            return answers.get(i);
        }else{
            return null;
        }
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
