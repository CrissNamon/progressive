package ru.danilarassokhin.progressive.manager;

public enum GameState {
    /**
     * Default state on before Story instance creation
     * <br>
     * Returns null
     */
    UNDEFINED,
    /**
     * Called on first Story instance creation
     * <br>
     * Returns Story object
     */
    INIT,

    START,
    /**
     * Called on {@link Story#setNext(StoryNode)}
     * <br>
     * Returns old node
     */
    NODE_TRANSITION_START,
    /**
     * Called on {@link Story#next()}
     * <br>
     * Returns new node
     */
    NODE_TRANSITION_END,
    /**
     * Called on {@link ru.danilarassokhin.progressive.basic.abstraction.AllSystemsCharacter#setLocation(StoryLocation, StoryAction, StoryAction)}
     * <br>
     * Returns old location
     */
    LOCATION_MOVE_START,
    /**
     * Called on {@link ru.danilarassokhin.progressive.basic.abstraction.AllSystemsCharacter#setLocation(StoryLocation, StoryAction, StoryAction)} if old location is not null
     * and new location has been set successfully
     */
    LOCATION_MOVE_COMPLETE,
    END
}
