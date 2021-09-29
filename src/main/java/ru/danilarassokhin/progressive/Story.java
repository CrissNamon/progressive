package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.annotations.StorySystemRequirement;
import ru.danilarassokhin.progressive.component.StoryNode;
import ru.danilarassokhin.progressive.exception.StoryException;
import ru.danilarassokhin.progressive.exception.StoryRequirementException;
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
     * @throws StoryRequirementException if some systems requirements has been violated
     * @return {@code startNode}
     */
    N begin(N startNode) throws StoryRequirementException;

    /**
     * Adds system to story
     * @param system System class to add
     * @param <S> System type
     * @throws StoryException if error occurred while adding system. Systems must have empty non-private constructor
     * @throws StoryRequirementException if {@code system} requirements has been violated
     * @return System object or null
     */
   <S extends StorySystem> S addSystem(Class<S> system) throws StoryException, StoryRequirementException;

    /**
     * Searches for system by it's class
     * @param systemClass System class to search
     * @param <S> System type
     * @return System object or null
     */
    <S extends StorySystem> S getSystem(Class<S> systemClass);

    /**
     * Returns map of all registered systems
     * @param <S> Story system type
     * @return Map of systems as system class - system
     */
    <S extends StorySystem> Map<Class<S>, S> getSystems();

    /**
     * Checks if system is registered in story
     * @param systemClass System class to check
     * @param <S> Story system type
     * @return true if system is registered in story
     */
    <S extends StorySystem> boolean hasSystem(Class<S> systemClass);

    /**
     * Searches for node registered in story by id
     * @param id Id to search
     * @return Node, null otherwise
     */
    N getNodeById(I id);


    /**
     * Checks if required systems by {@code systemClass} are registered in story
     * <br>
     * You can set a requirement by {@link ru.danilarassokhin.progressive.annotations.StorySystemRequirement} annotation
     * @param systemClass System class to check
     * @param <S> Story system type
     * @throws StoryRequirementException if requirements violated
     */
    default <S extends StorySystem> void checkSystemRequirements(Class<S> systemClass) throws StoryRequirementException {
       if(systemClass.isAnnotationPresent(StorySystemRequirement.class)) {
           StorySystemRequirement ann = systemClass.getAnnotation(StorySystemRequirement.class);
           Class<? extends StorySystem>[] reqs = ann.value();
           boolean cond = true;
           for(int i = 0; i < reqs.length && cond; ++i) {
               cond = hasSystem(reqs[i]);
               if(!cond) {
                   throw new StoryRequirementException("Requirement violation! System " + systemClass.toString()
                           + " requires " + reqs[i].toString() + " system which is not registered in story!");
               }
           }
       }
   }

}
