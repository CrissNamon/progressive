package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.data.StoryNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SimpleStoryNode implements StoryNode<Long, SimpleStoryNode, String, SimpleStoryNodeAnswer>, Serializable, AutoCloseable {

    private final Long id;
    private String text;
    private transient final List<SimpleStoryNodeAnswer> answers;

    protected SimpleStoryNode(Long id, String text, List<SimpleStoryNodeAnswer> answers) {
        this.id = id;
        this.text = text;
        this.answers = answers;
    }

    protected SimpleStoryNode(Long id, String text, SimpleStoryNodeAnswer... answers) {
        this.id = id;
        this.text = text;
        this.answers = new ArrayList<>();
        addAnswer(answers);
    }

    protected SimpleStoryNode(Long id, String text) {
        this.id = id;
        this.text = text;
        this.answers = new ArrayList<>();
    }

    protected SimpleStoryNode(Long id) {
        this.id = id;
        this.text = "";
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
    public SimpleStoryNode setContent(String content) {
        this.text = content;
        return this;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }

    @Override
    public void close() throws ClassCastException {

    }
}
