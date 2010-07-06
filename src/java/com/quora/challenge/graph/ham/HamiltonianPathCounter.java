package com.quora.challenge.graph.ham;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.quora.challenge.PathStatistics;
import com.quora.challenge.graph.AdjacencyList;
import com.quora.challenge.graph.Graph;
import com.quora.challenge.graph.ham.pruning.BiConnectedPruningStrategy;
import com.quora.challenge.graph.ham.pruning.ConnectedPruningStrategy;
import com.quora.challenge.graph.ham.pruning.EdgePruningStrategy;
import com.quora.challenge.graph.ham.pruning.AdmissibleDegreePruningStrategy;

/**
 * Counts the number of hamiltonian paths in a given graph.
 * 
 * @author mattt
 * @param <V>
 *            is the vertex type of the given graph.
 */
public class HamiltonianPathCounter<V> {

    // simple cache mapping a graph to the path count statistics associated with it.
    private final ConcurrentMap<Graph<V>, PathStatistics<V>> pathCounts = new MapMaker().softValues().expiration(30,
            TimeUnit.MINUTES).makeMap();

    private final BiConnectedPruningStrategy<V> biConnectedTester;

    @SuppressWarnings("unused")
    private final ConnectedPruningStrategy<V> connectedTester;
    @SuppressWarnings("unused")
    private final AdmissibleDegreePruningStrategy<V> degreeTester;
    @SuppressWarnings("unused")
    private final EdgePruningStrategy<V> edgePruningStrategy;

    /**
     * @param biConnectedTester
     *            is the {@link BiConnectedPruningStrategy} to use.
     * @param connectedTester
     *            is the {@link ConnectedPruningStrategy} to use.
     * @param degreeTester
     *            is the {@link AdmissibleDegreePruningStrategy} to use.
     * @param edgePruningStrategy
     *            is the {@link EdgePruningStrategy} to use.
     */
    public HamiltonianPathCounter(BiConnectedPruningStrategy<V> biConnectedTester,
            ConnectedPruningStrategy<V> connectedTester, AdmissibleDegreePruningStrategy<V> degreeTester,
            EdgePruningStrategy<V> edgePruningStrategy) {

        this.biConnectedTester = biConnectedTester;
        this.connectedTester = connectedTester;
        this.degreeTester = degreeTester;
        this.edgePruningStrategy = edgePruningStrategy;
    }

    /**
     * Finds the hamiltonian paths in the given graph.
     * 
     * @param graph
     *            is the graph to obtain the path count from.
     * @return the {@link PathStatistics} associated with this run.
     */
    public PathStatistics<V> findPaths(final Graph<V> graph) {
        if (graph == null)
            throw new NullPointerException();

        if (pathCounts.containsKey(graph)) {
            return pathCounts.get(graph);
        }

        final AtomicLong pathCount = new AtomicLong(0);
        final AtomicLong depth = new AtomicLong(0);

        final long startTime = System.currentTimeMillis();
        performPathCount(pathCount, depth, graph);
        final long elapsed = System.currentTimeMillis() - startTime;

        final PathStatistics<V> pathStatistics = new PathStatistics.Builder<V>()
                .depth(depth.get())
                .count(pathCount.get())
                .elapsedTime(elapsed)
                .graph(graph)
            .build();
        pathCounts.put(graph, pathStatistics);
        return pathStatistics;
    }

    /**
     * Performs the path count against the given graph.
     * 
     * @param pathCount
     *            is the number of hamiltonian paths that have been found.
     * @param depth
     *            is the search depth, excluding pruned searches.
     * @param graph
     *            is the graph to obtain the path count from.
     */
    private void performPathCount(final AtomicLong pathCount, final AtomicLong depth, final Graph<V> graph) {

        // get start and goal verticies
        final V start = graph.getStart();
        final V goal = graph.getGoal();

        // get the adjacency list
        final AdjacencyList<V> adjacencyList = graph.getGraph();

        // construct our start path
        final HashSet<V> path = Sets.newHashSet();
        path.add(start);

        // creating one thread pool per method invocation. Alternatively a shared thread pool 
        // could be passed in when the object is created.
        final ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            for (final V v : adjacencyList.getVerticesFromSource(start)) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        mainSearch(depth, pathCount, path, adjacencyList, v, goal);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // shouldn't happen
            throw new AssertionError(e);
        }
    }

    /**
     * Searches for hamiltonian paths, extended a new path to search for each neighbor of v.
     * 
     * @param pathCount
     *            is the number of hamiltonian paths that have been found thus far.
     * @param depth
     *            is the search depth, excluding pruned searches.
     * @param path
     *            is the current path.
     * @param graph
     *            is the graph to search.
     * @param vertex
     *            is the current vertex.
     * @param goal
     *            is the goal vertex.
     */
    private void mainSearch(final AtomicLong depth, final AtomicLong pathCount, final Set<V> path,
            final AdjacencyList<V> graph, final V vertex, final V goal) {

        // if we've visited it, return
        if (path.contains(vertex)) {
            return;
        }

        // extended the current path
        final Set<V> currentPath = Sets.newHashSet(path);
        currentPath.add(vertex);

        // the current vertex is the goal (may or may not be a hamiltonian path)
        if (vertex.equals(goal)) {
            // if we're at the goal and we've explored all verticies then we
            // have found a hamiltonian path
            if (graph.vertexCount() == currentPath.size()) {
                pathCount.incrementAndGet();
            }
            return;
        }

        /* 
         * // Use this adjacency list in in the subsequent pruning tests to test edge removal 
         *  final AdjacencyList<V> pruneGraph = edgePruningStrategy.removeNonDegree2NeighborEdges(graph, path, vertex, goal);
         */

        /*
         * // Valid degree check
         * if (!degreeTester.hasValidDegrees(path, pruneGraph, vertex, goal)) { return; }
         */

        // Biconnectivity check
        if (!biConnectedTester.isBiConnected(graph, vertex, path, goal)) {
            return;
        }

        /* 
         * // Connectivity check
         * if (!connectedTester.isConnected(path, graph, vertex)) { return; }
         */

        depth.incrementAndGet();
        for (final V w : graph.getVerticesFromSource(vertex)) {
            mainSearch(depth, pathCount, currentPath, graph, w, goal);
        }
    }
}
