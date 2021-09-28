package ru.danilarassokhin.progressive;

import ru.danilarassokhin.progressive.data.StoryNode;

import java.util.List;
import java.util.Map;


/**
 * StoryGraph represents scenery flow.
 * <br>
 * It contains {@link StoryNode} and their connections
 * @param <N> Your StoryNode class
 */
@Deprecated
public interface StoryGraph<N extends StoryNode> {

    Map<N, List<N>> getStoryGraph();

    N first();

    void addNode(N... node);

    void removeNode(N node);

    void addConnection(N from, N to);

    void removeConnection(N from, N to);


}
