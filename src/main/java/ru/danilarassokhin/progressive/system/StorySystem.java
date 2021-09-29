package ru.danilarassokhin.progressive.system;

import ru.danilarassokhin.progressive.component.StoryComponent;

import java.util.Map;
import java.util.Set;

/**
 * Represents some addition system of a story
 * @param <I> System's component id type
 * @param <C> System's component type
 */
public interface StorySystem<I, C extends StoryComponent> {

    /**
     * Returns all components registered in this system
     * @return Map of system's components as component class - component
     */
    Map<I, C> getComponents();

    /**
     * Adds component to system
     * @param id Component id
     * @return Empty component or null if id already exists
     */
    C addComponent(I id);

    /**
     * Removes component by id
     * @param id Component id to remove
     */
    void removeComponent(I id);

    /**
     * Returns requirements of this system
     * @return Set of required systems classes
     */
    Set<Class<? extends  StorySystem>> getRequirements();

    /**
     * Searches for component by id
     * @param id Component id to search
     * @return Component or null if not found
     */
    C getComponentById(I id);

}
