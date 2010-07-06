package com.quora.challenge.graph.ham.pruning;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.quora.challenge.graph.AdjacencyList;

/**
 * A pruning strategy that removes spurious edges in a given graph.
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type
 */
public class EdgePruningStrategy<V> {

    /**
     * When a vertex v in graph G has 2 neighbors of degree 2, will remove any additional edges adjacent to v. 
     * </p>
     * Specifically the method removes verticies according to the following theorem regarding hamiltonian cycles: 
     * </p>
     * 
     * <i>If vertex v has 2 neighbors a and b which are both of degree 2, then all edges (v, x), where x not in {a, b}
     * are not in any possible hamiltonian cycle.</i>
     * 
     * @param graph
     *            is the graph to traverse when removing edges.
     * @param currentPath
     *            is the current path. Any verticies in this path will not be examined when removing edges.
     * @param root
     *            is the root vertex.
     * @param goal
     *            is the goal vertex.
     * @return an {@link AdjacencyList} with spurious edges removed.
     */
    public AdjacencyList<V> removeNonDegree2NeighborEdges(final AdjacencyList<V> graph, final Set<V> currentPath,
            final V root, final V goal) {

        final AdjacencyList<V> pruneGraph = AdjacencyList.newInstance(graph);
        final Set<V> unexploredVerts = Sets.difference(graph.getVerticies(), currentPath);
        for (final V v : unexploredVerts) {

            int degree2Neighbors = 0;
            final Set<V> pruneNeighbors = new HashSet<V>();
            final Set<V> neighbors = Sets.newHashSet(Sets.difference(Sets.newHashSet(graph.getVerticesFromSource(v)),
                    currentPath));

            // if the vertex is a root add an additional goal neighbor, if it's a 
            // goal, add an additional root neighbor (this treats the hamiltonian
            // path as a hamiltonian cycle).
            PruningUtil.addCycleNeighbor(neighbors, v, root, goal);

            for (final V u : neighbors) {

                final Set<V> uNeighbors = Sets.newHashSet(Sets.difference(Sets.newHashSet(graph
                        .getVerticesFromSource(u)), currentPath));
                // if the vertex is a root add an additional goal neighbor, if it's a 
                // goal, add an additional root neighbor (this treats the hamiltonian
                // path as a hamiltonian cycle).
                PruningUtil.addCycleNeighbor(uNeighbors, u, root, goal);

                if (uNeighbors.size() != 2 || degree2Neighbors == 2) {
                    pruneNeighbors.add(u);
                } else {
                    degree2Neighbors++;
                }
            }
            if (Sets.difference(neighbors, pruneNeighbors).size() >= 2) {
                for (final V u : pruneNeighbors) {
                    pruneGraph.removeEdge(v, u);
                    pruneGraph.removeEdge(u, v);
                }
            }
        }
        return pruneGraph;
    }
}
