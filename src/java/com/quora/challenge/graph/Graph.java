package com.quora.challenge.graph;

/**
 * A graph with a start and goal node.
 * 
 * @author mattt
 * @param <V>
 *            is a vertex
 */
public class Graph<V> {

    private final AdjacencyList<V> graph;
    private final V start;
    private final V goal;

    /**
     * @param start
     *            is the start node. The start node is assumed to be included in the given graph.
     * @param goal
     *            is the goal node. The goal node is assumed to be included in the given graph.
     * @param graph
     *            is the internal graph representation.
     */
    public Graph(final V start, final V goal, final AdjacencyList<V> graph) {
        this.start = start;
        this.goal = goal;
        this.graph = graph;
    }

    /**
     * @return the start node
     */
    public V getStart() {
        return start;
    }

    /**
     * @return the internal graph representation.
     */
    public AdjacencyList<V> getGraph() {
        return graph;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Start vertex: " + start + "\n Goal vertex: " + goal + "\nGraph: " + graph;
    }

    /**
     * @return the goal node
     */
    public V getGoal() {
        return goal;
    }
}
