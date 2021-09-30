package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Story;
import ru.danilarassokhin.progressive.component.StoryState;
import ru.danilarassokhin.progressive.exception.StoryException;
import ru.danilarassokhin.progressive.exception.StoryRequirementException;
import ru.danilarassokhin.progressive.system.StorySystem;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleStory implements Story<Long, SimpleStoryNode>, Serializable {

    private transient static SimpleStory INSTANCE;

    private transient final Map<Long, SimpleStoryNode> storyNodes;
    private SimpleStoryNode currentNode;
    private SimpleStoryStateManager stateManager;
    private final Map<Class<? extends StorySystem>, StorySystem> systems;

    private SimpleStory() {
        this.storyNodes = new HashMap<>();
        this.stateManager = SimpleStoryStateManager.getInstance();
        systems = new HashMap<>();
        stateManager.setState(StoryState.INIT, this);
    }

    public static SimpleStory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStory();
        }
        return INSTANCE;
    }

    @Override
    public SimpleStoryNode begin(SimpleStoryNode startNode) throws StoryRequirementException{
        checkAllRequirements();
        currentNode = startNode;
        return currentNode;
    }

    @Override
    public <S extends StorySystem> S addSystem(Class<S> system) throws StoryException, StoryRequirementException{

        try {
            S systemObj = system.getDeclaredConstructor().newInstance();
            if(systemObj.getRequirements() != null) {
                checkSystemRequirements(system);
            }
            if(systems.putIfAbsent(system, systemObj) == null) {
                return systemObj;
            }else{
                return null;
            }
        } catch (InstantiationException | IllegalAccessException
        | InvocationTargetException | NoSuchMethodException e) {
            throw new StoryException("System registration failure! Check if your system has empty constructor");
        }
    }

    @Override
    public <S extends StorySystem> S getSystem(Class<S> systemClass) {
        SimpleObjectCaster objectCaster = new SimpleObjectCaster();
        return objectCaster.cast(systems.get(systemClass), systemClass, (s) -> {});
    }

    @Override
    public Map<Class<? extends StorySystem>, ? extends StorySystem> getSystems() {
        return systems;
    }

    @Override
    public <S extends StorySystem> boolean hasSystem(Class<S> systemClass) {
        return getSystem(systemClass) != null;
    }

    @Override
    public SimpleStoryNode getNodeById(Long id) {
        return storyNodes.getOrDefault(id, null);
    }

    private void checkAllRequirements() throws StoryRequirementException {
        Set<Class<? extends StorySystem>> keys = getSystems().keySet();
        for (Class<? extends StorySystem> key : keys) {
            checkSystemRequirements(key);
        }
    }

    @Override
    public SimpleStoryNode getCurrentNode() {
        return currentNode;
    }

    /**
     * Sets next node
     * <br>
     * Changes StateManager state to NODE_TRANSITION_START(current node) then sets new node
     * @param node Next node
     * @return {@code node}
     */
    @Override
    public SimpleStoryNode setNext(SimpleStoryNode node) {
        stateManager.setState(StoryState.NODE_TRANSITION_START, getCurrentNode());
        currentNode = node;
        return getCurrentNode();
    }

    @Override
    public Map<Long, SimpleStoryNode> getStoryNodes() {
        return storyNodes;
    }

    @Override
    public SimpleStoryNode addStoryNode(Long id) {
        SimpleStoryNode node = new SimpleStoryNode(id);
        if(storyNodes.putIfAbsent(id, node) == null) {
            return node;
        }else{
            return null;
        }
    }

    /**
     * Go to next node
     * <br>
     * Changes StateManager state to NODE_TRANSITION_END(new node)
     * @return next node
     */
    @Override
    public SimpleStoryNode next() {
        stateManager.setState(StoryState.NODE_TRANSITION_END, getCurrentNode());
        return currentNode;
    }
}
