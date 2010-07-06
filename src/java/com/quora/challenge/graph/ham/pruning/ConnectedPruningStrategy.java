package com.quora.challenge.graph.ham.pruning;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quora.challenge.graph.AdjacencyList;

/**
 * A pruning strategy that tests the connectivity of a graph. </p> For more information see: <a
 * href="http://en.wikipedia.org/wiki/Connectivity_(graph_theory)"
 * >http://en.wikipedia.org/wiki/Connectivity_(graph_theory)</a>
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type.
 */
public class ConnectedPruningStrategy<V> {

    /**
     * Visits the neighbor paths of the current vertex v, generating a {@link ConnectedMapping} which maps a neighbor of
     * v to the set of verticies accessible from it. </p> Any verticies in the given current path P will not be visited.
     * 
     * @param currentPath
     *            is the current path. Any verticies in this path will not be visited.
     * @param graph
     *            is the graph to visit.
     * @param currentVertex
     *            is the current vertex to visit neighbors for.
     * @return a {@link ConnectedMapping} which maps a neighbor of v to the set of verticies accessible from it.
     */
    private ConnectedMapping visitNeighborPaths(final Set<V> currentPath, final AdjacencyList<V> graph,
            final V currentVertex) {

        assert !currentPath.contains(currentVertex);

        final List<V> neighborList = Lists.newArrayList();
        final Map<V, Set<V>> unvisitedMapping = HashBiMap.create();

        for (final V neighbor : graph.getVerticesFromSource(currentVertex)) {
            if (!currentPath.contains(neighbor)) {
                final Set<V> pathSet = Sets.newHashSet();
                unvisitedMapping.put(neighbor, pathSet);
                neighborList.add(neighbor);
                visit(pathSet, currentPath, graph, neighbor, currentVertex);
            }
        }
        return new ConnectedMapping(currentVertex, neighborList, unvisitedMapping);
    }

    /**
     * Performs a depth first search, first visiting v, then visiting any neighbors of v.
     * 
     * @param visitedSubPath
     *            is the {@link Set} of verticies visited by this method.
     * @param currentPath
     *            is the current path (set of previously explored verticies).
     * @param adjacencyList
     *            is the {@link AdjacencyList} associated with this graph.
     * @param v
     *            is the current vertex.
     * @param root
     *            is the root (i.e. starting) vertex
     */
    private void visit(final Set<V> visitedSubPath, final Set<V> currentPath, final AdjacencyList<V> adjacencyList,
            final V v, final V root) {
        if (currentPath.contains(v) || visitedSubPath.contains(v)) {
            return;
        }
        if (v.equals(root)) {
            return;
        }

        visitedSubPath.add(v);

        for (final V n : adjacencyList.getVerticesFromSource(v)) {
            visit(visitedSubPath, currentPath, adjacencyList, n, root);
        }
    }

    /**
     * Using the given graph G, path P, and vertex v, will determine if every vertex u in G can be reached from v. Any
     * verticies in P will be excluded from the connectivity test.
     * </p>
     * 
     * @param currentPath
     *            is the current path (set of explored verticies). Any verticies in this path will be excluded from the
     *            connectivity test.
     * @param graph
     *            is the graph to test connectivity for.
     * @param currentVertex
     *            is the current vertex.
     * @return true if the graph is connected (that is, all verticies in G - P are reachable from the current vertex),
     *         false otherwise.
     */
    public boolean isConnected(final Set<V> currentPath, final AdjacencyList<V> graph, final V currentVertex) {

        assert !currentPath.contains(currentVertex);
        
        // The way I test for connectivity is by generating the set of verticies
        // accessible from each neighbor of v, then check each set for
        // disjointedness. If any paths are disjoint, (i.e. path1 intersect
        // path2 = empty set) then all verticies are not accessible from v and
        // false is returned.
        final ConnectedMapping connectedMapping = visitNeighborPaths(currentPath, graph, currentVertex);

        final Map<V, Set<V>> roomMap = connectedMapping.getPathMap();
        final List<V> roomList = connectedMapping.getNeighborList();

        // we have two or three paths
        if (roomMap.size() > 1) {
            final Set<V> path1 = roomMap.get(roomList.get(0));
            final Set<V> path2 = roomMap.get(roomList.get(1));

            // we have two paths
            if (roomMap.size() == 2) {
                // is path1 and path2 disjoint?
                if (Sets.intersection(path1, path2).isEmpty())
                    return false;
            }

            // we have three paths
            if (roomMap.size() == 3) {
                final Set<V> path3 = roomMap.get(roomList.get(2));

                // is path1 and path3 disjoint?
                if (Sets.intersection(path3, path1).isEmpty()) {
                    return false;
                }
                // is path2 and path3 disjoint?
                if (Sets.intersection(path3, path2).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Associates a list of neighbors of a given vertex with the paths (that is, the set of verticies) accessible from
     * that neighbor. 
     * </p> 
     * 
     * For instance if v = 0 and has neighbors {1,2,3}, where {4,5} can be accessed from 1, {6} can
     * be accessed from 2, and {7,8} can be accessed from 3, the mapping might look like: 
     * </p> 
     * 
     * 1 -> {4,5}, 
     * 2 -> {6}, 
     * 3 -> {7,8} 
     * </p> 
     * 
     * This mapping is used to test for disjointedness between accessible paths from v.
     * 
     * @author mattt
     */
    private final class ConnectedMapping {

        private final Map<V, Set<V>> pathMap;
        private final List<V> neighborList;
        private final V vertex;

        /**
         * @param vertex
         *            is the vertex that this mapping is associated with.
         * @param neighborList
         *            is the list of neighbors for the given vertex.
         * @param pathMap
         *            is a neighbor -> path mapping for each neighbor in the given neighbor list.
         */
        public ConnectedMapping(final V vertex, final List<V> neighborList, final Map<V, Set<V>> pathMap) {
            assert !neighborList.isEmpty();
            assert !pathMap.isEmpty();
            assert pathMap.keySet().containsAll(neighborList);

            this.vertex = vertex;
            this.neighborList = neighborList;
            this.pathMap = pathMap;
        }

        /**
         * @return the path map
         */
        public Map<V, Set<V>> getPathMap() {
            return Collections.unmodifiableMap(pathMap);
        }

        /**
         * @return the list of neighbors associated with this mapping
         */
        public List<V> getNeighborList() {
            return Collections.unmodifiableList(neighborList);
        }

        /**
         * @return the vertex
         */
        @SuppressWarnings("unused")
        public V getVertex() {
            return vertex;
        }
    }
}
