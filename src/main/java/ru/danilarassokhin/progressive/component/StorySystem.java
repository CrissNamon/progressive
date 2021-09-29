package ru.danilarassokhin.progressive.component;

import java.util.Map;
import java.util.Set;

public interface StorySystem<I, C extends StoryComponent> {

    Map<I, C> getComponents();
    C addComponent(I id);
    void removeComponent(I id);
    Set<Class<? extends  StorySystem>> getRequirements();
    C getComponentById(I id);

}
