package ru.danilarassokhin.progressive;

/**
 * Represents story state for state manager
 */
public enum StoryState {
    INIT,
    NODE_TRANSITION_START,
    NODE_TRANSITION_END,
    LOCATION_MOVE_START,
    LOCATION_MOVE_COMPLETE,
    END
}
