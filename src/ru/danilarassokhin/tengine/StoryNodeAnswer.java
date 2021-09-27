package ru.danilarassokhin.tengine;

/**
 * Represents story node answer
 * @param <C> Answer content type
 */
public interface StoryNodeAnswer<C> {
    /**
     * @return Answer content
     */
    C getContent();

    /**
     * Makes answer action
     */
    void onAnswer();
}
