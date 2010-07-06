package com.quora.challenge.graph.ham.pruning;

import java.util.Collections;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.quora.challenge.graph.AdjacencyList;

public class ValidDegreePruningStrategyTest {

    private AdmissibleDegreePruningStrategy<Integer> strategy;

    @BeforeClass
    public void setup() {
        strategy = new AdmissibleDegreePruningStrategy<Integer>();
    }

    @Test
    public void hasValidDegreesSimplePathTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(1, 0);

        al.addEdge(1, 2);
        al.addEdge(2, 1);

        // the reason that we don't have to add an undirected edge from 0 <-> 2
        // is because this gets added as we are walking the graph.

        Set<Integer> currentPath = Collections.emptySet();
        Assert.assertTrue(strategy.hasValidDegrees(currentPath, al, 0, 2));
    }

    @Test
    public void notValidDegreesSimplePathTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 3);

        al.addEdge(1, 0);
        al.addEdge(1, 2);
        al.addEdge(1, 4);

        al.addEdge(2, 5);
        al.addEdge(2, 1);

        al.addEdge(3, 0);
        al.addEdge(3, 4);

        al.addEdge(4, 3);
        al.addEdge(4, 1);
        al.addEdge(4, 5);

        al.addEdge(5, 4);
        al.addEdge(5, 2);

        // starting at 0 all degrees are valid
        Set<Integer> emptyPath = Collections.emptySet();
        Assert.assertTrue(strategy.hasValidDegrees(emptyPath, al, 0, 5));

        // if we extended the path to {0,1} with a current vertex of 4, then 2
        // becomes isolated because d(2) = 1
        Set<Integer> extendedPath = Sets.newHashSet(0, 1);
        Assert.assertFalse(strategy.hasValidDegrees(extendedPath, al, 4, 5));

    }

    @Test
    public void notValidDegrees3Degree2NeighborsTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 2);
        al.addEdge(0, 3);

        al.addEdge(1, 0);
        al.addEdge(1, 4);

        al.addEdge(2, 0);
        al.addEdge(2, 4);

        al.addEdge(2, 0);
        al.addEdge(2, 4);

        al.addEdge(4, 1);
        al.addEdge(4, 2);
        al.addEdge(4, 3);

        Set<Integer> emptyPath = Collections.emptySet();
        Assert.assertFalse(strategy.hasValidDegrees(emptyPath, al, 0, 4));
    }
}
