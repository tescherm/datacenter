package com.quora.challenge.graph.ham.pruning;

import java.util.Collections;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.quora.challenge.graph.AdjacencyList;

public class EdgePruningStrategyTest {

    private EdgePruningStrategy<Integer> strategy;
    private AdjacencyList<Integer> pruneGraph;

    @BeforeClass
    public void setup() {
        strategy = new EdgePruningStrategy<Integer>();
    }

    @Test
    public void singleEdgePruningTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 2);
        al.addEdge(0, 3);

        al.addEdge(1, 0);
        al.addEdge(1, 2);

        al.addEdge(2, 1);
        al.addEdge(2, 0);

        al.addEdge(3, 0);
        al.addEdge(3, 4);
        al.addEdge(3, 5);

        al.addEdge(4, 3);
        al.addEdge(4, 5);

        al.addEdge(5, 3);
        al.addEdge(5, 4);

        Set<Integer> currentPath = Collections.emptySet();
        pruneGraph = strategy.removeNonDegree2NeighborEdges(al, currentPath, 0, 3);

        Assert.assertTrue(pruneGraph.edgeCount() < al.edgeCount());
        Assert.assertEquals(pruneGraph.vertexCount(), al.vertexCount());

        Assert.assertEquals(pruneGraph.getVerticesFromSource(0).size(), 2);
    }

    @Test(dependsOnMethods = { "singleEdgePruningTest" })
    public void testCascadePruning() {
        AdjacencyList<Integer> nextPruneGraph = AdjacencyList.newInstance(pruneGraph);

        Set<Integer> currentPath = Collections.emptySet();
        nextPruneGraph = strategy.removeNonDegree2NeighborEdges(pruneGraph, currentPath, 0, 3);
        Assert.assertEquals(nextPruneGraph.edgeCount(), pruneGraph.edgeCount());
    }

    @Test
    public void noEdgePrunedTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 2);
        al.addEdge(0, 3);

        al.addEdge(1, 0);
        al.addEdge(1, 2);
        al.addEdge(1, 3);

        al.addEdge(2, 1);
        al.addEdge(2, 0);
        al.addEdge(2, 3);

        al.addEdge(3, 1);
        al.addEdge(3, 0);
        al.addEdge(3, 2);

        Set<Integer> currentPath = Collections.emptySet();
        AdjacencyList<Integer> pruneGraph = strategy.removeNonDegree2NeighborEdges(al, currentPath, 0, 3);

        Assert.assertEquals(pruneGraph.edgeCount(), al.edgeCount());
        Assert.assertEquals(pruneGraph.vertexCount(), al.vertexCount());
    }
}
