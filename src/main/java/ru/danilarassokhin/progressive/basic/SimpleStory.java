package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Story;
import ru.danilarassokhin.progressive.component.StoryState;
import ru.danilarassokhin.progressive.component.StorySystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
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
        stateManager.setState(StoryState.INIT, this);
        systems = new HashMap<>();
    }

    public static SimpleStory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStory();
        }
        return INSTANCE;
    }

    @Override
    public SimpleStoryNode begin(SimpleStoryNode startNode) {
        currentNode = startNode;
        return currentNode;
    }

    @Override
    public <S extends StorySystem> S addSystem(Class<S> system) {
        try {
            S systemObj = system.getDeclaredConstructor().newInstance();
            if(systemObj.getRequirements() != null) {
                if(!checkSystemRequirements(systemObj.getRequirements())) {
                    return null;
                }
            }
            if(systems.putIfAbsent(system, systemObj) == null) {
                return systemObj;
            }else{
                return null;
            }
        }catch (Exception e) {
            return null;
        }
    }

    private boolean checkSystemRequirements(Set<Class<? extends StorySystem>> reqs) {
        Iterator<Class<? extends StorySystem>> iterator = reqs.iterator();
        boolean allReqs = true;
        while(iterator.hasNext() && allReqs) {
            allReqs = systems.containsKey(iterator.next());
        }
        return allReqs;
    }

    @Override
    public <S extends StorySystem> S getSystem(Class<S> systemClass) {
        SimpleObjectCaster objectCaster = new SimpleObjectCaster();
        return objectCaster.cast(systems.get(systemClass), systemClass, (s) -> {});
    }

    @Override
    public SimpleStoryNode getNodeById(Long id) {
        return storyNodes.getOrDefault(id, null);
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
