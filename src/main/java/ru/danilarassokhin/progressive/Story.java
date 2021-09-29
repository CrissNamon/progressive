package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.component.*;
import ru.danilarassokhin.progressive.system.StorySystem;

import java.util.Map;


/**
 * Represents game story
 * <br>Story must be Singleton!
 * @param <N> Story nodes type {@link StoryNode}
 * @param <I> Story's node id type
 */
public interface Story<I, N extends StoryNode> {

    /**
     * Returns current node
     * @return current story node
     */
    N getCurrentNode();

    /**
     * Go to next node
     * @return next node
     */
    N next();

    /**
     * Sets next node
     * @param node Next node
     * @return {@code node}
     */
    N setNext(N node);


    /**
     * Returns all nodes registered in story
     * @return Map of nodes as nodeId - node
     */
    Map<I, N> getStoryNodes();

    /**
     * Adds node to story
     * @param id Node id to add
     * @return Empty node or null if id already exists
     */
    N addStoryNode(I id);

    /**
     * Sets current node in Story to {@code startNode}
     * @param startNode Node to set
     * @return {@code startNode}
     */
    N begin(N startNode);

    /**
     * Adds system to story
     * @param system System class to add
     * @param <S> System type
     * @return System object or null
     */
   <S extends StorySystem> S addSystem(Class<S> system);

    /**
     * Searches for system by it's class
     * @param systemClass System class to search
     * @param <S> System type
     * @return System object or null
     */
    <S extends StorySystem> S getSystem(Class<S> systemClass);

    /**
     * Searches for node registered in story by id
     * @param id Id to search
     * @return Node, null otherwise
     */
    N getNodeById(I id);

}
