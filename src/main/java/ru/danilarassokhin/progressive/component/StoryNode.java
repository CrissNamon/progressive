package ru.danilarassokhin.progressive.component;

import java.util.List;

/**
 * Represents story node
 * @param <ID> Node id type
 * @param <C> Node content type
 * @param <A> Node answer type
 */
public interface StoryNode<ID, N, C, A extends StoryNodeAnswer> {

    /**
     * Returns node's content
     * @return Node content
     */
    C getContent();

    /**
     * Returns node id
     * @return Node id
     */
    ID getId();

    /**
     * Returns all answers connected with this node
     * @return Node answers
     */
    List<A> getAnswers();

    /**
     * Adds answers to node
     * @param answer Answers to addd
     */
    void addAnswer(A... answer);

    /**
     * Checks if node has any answers
     * @return true if node has any answers
     */
    boolean hasAnswers();

    /**
     * Gets answer by index
     * @param i Answer index
     * @return Answer of index {@code i}
     */
    A answer(int i);

    /**
     * Sets content to node
     * @param content New content
     * @return this node
     */
    N setContent(C content);

}
