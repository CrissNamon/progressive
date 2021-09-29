package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.component.*;

import java.util.Map;


/**
 * Represents game story
 * <br>Story must be Singleton!
 * @param <N> Story nodes type {@link StoryNode}
 * @param <C> Story characters type {@link StoryCharacter}
 * @param <L> Story locations type {@link StoryLocation}
 * @param <I> Story item type {@link StoryItem}
 * @param <Q> Story quest type {@link StoryQuest}
 */
public interface Story<ID, N extends StoryNode> {

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
    Map<ID, N> getStoryNodes();

    /**
     * Adds node to story
     * @param id Node id to add
     * @return Empty node or null if id already exists
     */
    N addStoryNode(ID id);

    /**
     * Sets current node in Story to {@code startNode}
     * @param startNode Node to set
     * @return {@code startNode}
     */
    N begin(N startNode);

   <S extends StorySystem> S addSystem(Class<S> system);

    <S extends StorySystem> S getSystem(Class<S> systemClass);

    /**
     * Searches for node registered in story by id
     * @param id Id to search
     * @return Node, null otherwise
     */
    N getNodeById(ID id);

}
