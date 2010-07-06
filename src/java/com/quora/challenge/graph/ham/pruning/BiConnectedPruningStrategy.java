package com.quora.challenge.graph.ham.pruning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.quora.challenge.graph.AdjacencyList;

/**
 * A pruning strategy that tests the biconnectivity of a graph (that is, it is a connected graph with no articulation
 * points). 
 * 
 * </p> 
 * 
 * For more information see: 
 * 
 * <a href="http://en.wikipedia.org/wiki/Biconnected_component">http://en.wikipedia.org/wiki/Biconnected_component</a>
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type.
 */
public class BiConnectedPruningStrategy<V> {

    /**
     * Using the given graph G, will determine if any articulation points exist. Any verticies in the current path P
     * will be excluded from the biconnectivity test.
     * 
     * </p> 
     * 
     * An articulation point is a vertex in a graph which when removed, increases the number of connected components 
     * in the graph (for more information, see: 
     * 
     * <a href="http://en.wikipedia.org/wiki/Articulation_vertex">http://en.wikipedia.org/wiki/Articulation_vertex</a>
     * 
     * @param graph
     *            is the graph to test biconnectivity for.
     * @param v
     *            is the current vertex.
     * @param currentPath
     *            is the current path (that is, the set of previously explored verticies). Any verticies in this path
     *            will be excluded from the biconnectivity test.
     * @return true if the graph is biconnected (in other words no articulation points exist), false otherwise.
     */
    public boolean isBiConnected(final AdjacencyList<V> graph, final V v, final Set<V> currentPath, final V goal) {

        assert !currentPath.contains(v);

        final Set<V> visited = Sets.newHashSet();
        // the set of articulation verticies that were found when conducting the test.
        final Set<V> articulationVerticies = Sets.newHashSet();
        int depth = 0;

        final BiConnectedVertexMapping nodeStats = new BiConnectedVertexMapping();

        // To test for biconnectivity I'm using Hopcroft and Tarjan's modified depth first search algorithm. For more
        // information, see http://en.wikipedia.org/wiki/Biconnected_component
        this.visit(articulationVerticies, currentPath, depth, nodeStats, visited, graph, v, v, goal);
        return articulationVerticies.isEmpty() ? true : false;
    }

    /**
     * Performs a depth first search, first visiting v, then visiting any neighbors of v. If an articulation vertex is
     * found, it is added to the given list of articulation verticies.
     * 
     * @param articulationVerticies
     *            is the {@link Set} of articulation verticies that have been found thus far.
     * @param currentPath
     *            is the current path (set of previously explored verticies).
     * @param depth
     *            is the current search depth.
     * @param nodeStats
     *            is the current biconnectivity vertex mappings associated with this search
     * @param adjacencyList
     *            is the {@link AdjacencyList} associated with this graph.
     * @param v
     *            is the current vertex.
     * @param root
     *            is the root (i.e. starting) vertex.
     * @param goal
     *            is the goal vertex.
     */
    private void visit(final Set<V> articulationVerticies, final Set<V> currentPath, int depth,
            final BiConnectedVertexMapping nodeStats, final Set<V> visited, final AdjacencyList<V> graph,
            final V vertex, final V root, final V goal) {

        // since we don't care about finding all articulation verticies, we
        // return once we found one.
        if (!articulationVerticies.isEmpty()) {
            return;
        }
        if (currentPath.contains(vertex) && !vertex.equals(root)) {
            return;
        }

        visited.add(vertex);

        depth++;

        nodeStats.setDepth(vertex, depth);
        nodeStats.setLow(vertex, depth);

        final HashSet<V> neighbors = Sets.newHashSet(graph.getVerticesFromSource(vertex));
        // if the vertex is a root add an additional goal neighbor, if it's a 
        // goal, add an additional root neighbor (this treats the hamiltonian
        // path as a hamiltonian cycle).
        PruningUtil.addCycleNeighbor(neighbors, vertex, root, goal);

        for (final V n : neighbors) {
            if (currentPath.contains(n))
                continue;

            if (!visited.contains(n)) {

                nodeStats.incrementChildCount(vertex);
                visit(articulationVerticies, currentPath, depth, nodeStats, visited, graph, n, root, goal);

                // since we don't care about finding all articulation verticies,
                // we return once we found one.
                if (!articulationVerticies.isEmpty())
                    return;

                final Integer num = nodeStats.getDepth(vertex);
                final Integer low = nodeStats.getLow(n);

                // we are at the root
                if (depth == 1) {
                    // a root is a cutpoint is it has more than one child
                    if (nodeStats.getChildCount(vertex) >= 2) {
                        articulationVerticies.add(vertex);
                    }
                    // we are at some other node
                } else if (low >= num) {
                    articulationVerticies.add(vertex);
                }

                // if the n's low is less than the current low, update it.
                nodeStats.setLow(vertex, Math.min(nodeStats.getLow(vertex), nodeStats.getLow(n)));
            } else if (nodeStats.getDepth(n) <= nodeStats.getDepth(vertex)) {
                // if the current depth less than the current low, update it.
                nodeStats.setLow(vertex, Math.min(nodeStats.getLow(vertex), nodeStats.getDepth(n)));
            }
        }
    }

    /**
     * Data structure which consolidates the mappings used in a biconnectivity test. Tracks the vertex depth in the dfs
     * search tree, the low value of a vertex, and how many children it has.
     * 
     * @author mattt
     */
    private class BiConnectedVertexMapping {

        // maps a vertex to it's depth in the search tree
        private final Map<V, Integer> depths = new HashMap<V, Integer>();

        // maps a vertex to it's lowest depth of any neighbor
        // or descendant
        private final Map<V, Integer> lows = new HashMap<V, Integer>();

        // vertex -> number of children mapping
        private final Map<V, Integer> children = new HashMap<V, Integer>();

        public Integer getChildCount(final V vertex) {
            final Integer childCount = children.get(vertex);
            if (childCount == null) {
                return 0;
            }
            return childCount;
        }

        public void incrementChildCount(final V vertex) {
            final Integer childCount = children.get(vertex);
            if (childCount == null) {
                children.put(vertex, 1);
            } else {
                children.put(vertex, childCount + 1);
            }
        }

        public void setDepth(final V vertex, final Integer depth) {
            depths.put(vertex, depth);
        }

        public Integer getDepth(final V vertex) {
            final Integer depth = depths.get(vertex);
            if (depth == null) {
                return 0;
            }
            return depth;
        }

        public void setLow(final V vertex, final Integer low) {
            lows.put(vertex, low);
        }

        public Integer getLow(final V vertex) {
            final Integer low = lows.get(vertex);
            if (low == null) {
                return 0;
            }
            return low;
        }
    }
}
