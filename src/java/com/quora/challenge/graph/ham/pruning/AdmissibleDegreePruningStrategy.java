package com.quora.challenge.graph.ham.pruning;

import java.util.Set;

import com.google.common.collect.Sets;
import com.quora.challenge.graph.AdjacencyList;

/**
 * A pruning strategy that ensures that each vertex in a given graph has a valid degree.
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type
 */
public class AdmissibleDegreePruningStrategy<V> {

    /**
     * Determines if a vertex v has a degree of 1 (that is, it has exactly 1 edge incident to it).
     * </p>
     * 
     * Specifically the method performs the degree check according to the following theorem regarding hamiltonian
     * cycles: 
     * </p> 
     * 
     * <i>In a graph with a hamiltonian cycle the degree of each vertex must be >= 2.</i>
     * 
     * @param currentPath
     *            is the current path. Any verticies in this path will not be included in the degree check.
     * @param graph
     *            is the graph to check.
     * @param neighbors
     *            is the {@link Set} of neighbors incident to v.
     * @return true if the vertex has a degree of 1, false otherwise.
     */
    public boolean isDegreeOne(final Set<V> currentPath, final AdjacencyList<V> graph, final Set<V> neighbors) {
        // less than one neighbor?
        if (Sets.difference(neighbors, currentPath).size() < 2) {

            // this check is for when the current graph has less than 3
            // verticies
            if (graph.vertexCount() - currentPath.size() != 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a vertex v has three degree 2 neighbors. 
     * </p> 
     * 
     * Specifically the method performs the degree check according to the following theorem 
     * regarding hamiltonian cycles: 
     * </p> 
     * 
     * <i>If a vertex has three neighbors of degree 2, that graph cannot contain a hamiltonian cycle.</i>
     * 
     * @param currentPath
     *            is the current path. Any verticies in this path will not be included in the degree check.
     * @param graph
     *            is the graph to check.
     * @param neighbors
     *            is the {@link Set} of neighbors incident to v.
     * @param root
     *            is the root vertex.
     * @param goal
     *            is the goal vertex.
     * @return true if the vertex has three degree 2 neighbors, false otherwise.
     */
    public boolean has3Degree2Neighbors(final Set<V> currentPath, final AdjacencyList<V> graph, final Set<V> neighbors,
            final V root, final V goal) {
        int degree2Neighbors = 0;
        final  Set<V> newNeighbors = Sets.difference(neighbors, currentPath);
        // we must have at least three neighbors to have three degree 2 neighbors 
        if (newNeighbors.size() >= 3) {
            for (final V w : newNeighbors) {

                // obtain the neighbors of w
                final Set<V> wNeighbors = Sets.newHashSet(graph.getVerticesFromSource(w));
                // if the vertex is a root add an additional goal neighbor, if it's a 
                // goal, add an additional root neighbor (this treats the hamiltonian
                // path as a hamiltonian cycle).
                PruningUtil.addCycleNeighbor(wNeighbors, w, root, goal);

                if (Sets.difference(wNeighbors, currentPath).size() == 2) {
                    degree2Neighbors++;
                }
            }

            if (degree2Neighbors > 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs various vertex degree checks against each vertex in the given graph.
     * 
     * @param currentPath
     *            is the current path. Any verticies in this path will not be included in a degree check.
     * @param graph
     *            is the graph to check.
     * @param root
     *            is the root vertex.
     * @param goal
     *            is the goal vertex.
     * @return true if each vertex in the given graph has a valid degree, false otherwise.
     */
    public boolean hasValidDegrees(final Set<V> currentPath, final AdjacencyList<V> graph, final V v, final V goal) {
        assert !currentPath.contains(v);

        final Set<V> visited = Sets.newHashSet();
        final Set<V> invalidDegreeVerticies = Sets.newHashSet();

        this.visit(invalidDegreeVerticies, visited, currentPath, graph, v, v, goal);
        return invalidDegreeVerticies.isEmpty() ? true : false;
    }

    /**
     * Performs a depth first search, first visiting v, then visiting any neighbors of v. If a vertex with an invalid
     * degree is found, it is added to the given list of invalid verticies.
     * 
     * @param invalidDegreeVerticies
     *            is the {@link Set} of invalid verticies that have been found thus far.
     * @param visited
     *            is the set of visited vertcies.
     * @param currentPath
     *            is the current path (set of previously explored verticies).
     * @param adjacencyList
     *            is the {@link AdjacencyList} associated with this graph.
     * @param vertex
     *            is the current vertex.
     * @param root
     *            is the root (i.e. starting) vertex.
     * @param goal
     *            is the goal vertex.
     */
    private void visit(final Set<V> invalidDegreeVerticies, final Set<V> visited, final Set<V> currentPath,
            final AdjacencyList<V> graph, final V vertex, final V root, final V goal) {
        if (currentPath.contains(vertex)) {
            return;
        }

        final Set<V> neighbors = Sets.newHashSet(graph.getVerticesFromSource(vertex));
        // if the vertex is a root add an additional goal neighbor, if it's a 
        // goal, add an additional root neighbor (this treats the hamiltonian
        // path as a hamiltonian cycle).
        PruningUtil.addCycleNeighbor(neighbors, vertex, root, goal);

        if (isDegreeOne(currentPath, graph, neighbors)) {
            invalidDegreeVerticies.add(vertex);
            // since we're not interested in tracking all pruned verticies, we
            // can return once we found one.
            return;
        }

        if (has3Degree2Neighbors(currentPath, graph, neighbors, root, goal)) {
            invalidDegreeVerticies.add(vertex);
            // since we're not interested in tracking all pruned verticies, we
            // can return once we found one.
            return;
        }

        visited.add(vertex);

        for (final V n : neighbors) {
            if (!visited.contains(n)) {
                visit(invalidDegreeVerticies, visited, currentPath, graph, n, root, goal);
            }
        }
    }
}
