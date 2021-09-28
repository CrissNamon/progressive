package ru.danilarassokhin.progressive;

import java.util.List;

/**
 * Represents story node
 * @param <ID> Node id type
 * @param <C> Node content type
 * @param <A> Node answer type
 */
public interface StoryNode<ID, C, A extends StoryNodeAnswer> {

    /**
     * @return Node content
     */
    C getContent();

    /**
     * @return Node id
     */
    ID getId();

    /**
     * @return Node answers
     */
    List<A> getAnswers();

    /**
     * Adds answers to node
     * @param answer Answers to addd
     */
    void addAnswer(A... answer);

    /**
     * @return true if node has any answers
     */
    boolean hasAnswers();

    /**
     * Gets answer by index
     * @param i Answer index
     * @return Answer of index {@code i}
     */
    A answer(int i);

}
