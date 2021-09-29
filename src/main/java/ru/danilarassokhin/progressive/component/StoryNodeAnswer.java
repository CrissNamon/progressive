package ru.danilarassokhin.progressive.component;

/**
 * Represents story node answer
 * @param <C> Answer content type
 */
public interface StoryNodeAnswer<C> {
    /**
     * Returns answer content
     * @return Answer content
     */
    C getContent();

    /**
     * Makes answer action
     */
    void onAnswer();
}
