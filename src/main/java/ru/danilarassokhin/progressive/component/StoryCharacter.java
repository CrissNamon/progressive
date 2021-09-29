package ru.danilarassokhin.progressive.component;

import ru.danilarassokhin.progressive.lambdas.StoryActionObject;

/**
 * Represents story character
 * @param <I> Character ID type
 * @param <AN> Character action id type
 */
public interface StoryCharacter<I, C, AN> extends StoryComponent<I> {

    /**
     * Returns character id
     * @return character id
     */
    I getId();

    /**
     * Returns character name
     * @return character name
     */
    String getName();

    /**
     * Sets character name
     * @param name new name
     * @return This character
     */
    C setName(String name);

    /**
     * Returns character's health
     * @return character's current health
     */
    float getHealth();

    /**
     * Sets character health
     * @param health new health value
     * @return This character
     */
    C setHealth(float health);

    /**
     * Adds health to character
     * @param add health to add
     */
    void addHealth(float add);

    /**
     * Does character action with {@code actionName}
     * @param actionName Action name to get
     * @param <O> Action param type to pass
     * @param param Action parameter
     * @return true if action is done
     */
    <O> boolean action(AN actionName, O param);

    /**
     * Does character action with {@code actionName} without parameters
     * @param actionName Action name to get
     * @return Action or null
     */
    boolean action(AN actionName);

    /**
     * Adds action to character
     * @param actionName Action name to add
     * @param action Action to add
     * @param <O> Action param type to pass
     * @return true if action doesn't exists already
     */
    <O> boolean addAction(AN actionName, StoryActionObject<O> action);

}
