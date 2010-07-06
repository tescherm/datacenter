package com.quora.challenge.graph.ham.pruning;

import java.util.Set;

/**
 * Contains helper methods related to pruning strategies.
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type
 */
public final class PruningUtil<V> {

    /**
     * suppress default constructor
     */
    private PruningUtil() {
        throw new AssertionError();
    }

    /**
     * If the current vertex is a root, adds the goal vertex to the set of neighbors. If it's a goal, adds the root
     * vertex to the set of neighbors. </p> This method is used to transform a potential hamiltonian path to a
     * hamiltonian cycle during pruning operations that act against hamiltonain cycles rather than paths.
     * 
     * @param <V>
     *            is the vertex type
     * @param neighbors
     *            is the {@link Set} of neighbors to add to.
     * @param vertex
     *            is the current vertex.
     * @param root
     *            is the root vertex.
     * @param goal
     *            is the goal vertex.
     */
    public static <V> void addCycleNeighbor(final Set<V> neighbors, final V vertex, final V root, final V goal) {
        if (vertex.equals(root)) {
            neighbors.add(goal);
        }
        if (vertex.equals(goal)) {
            neighbors.add(root);
        }
    }

}
