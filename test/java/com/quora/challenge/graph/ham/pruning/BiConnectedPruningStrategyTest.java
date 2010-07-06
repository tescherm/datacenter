package com.quora.challenge.graph.ham.pruning;

import java.util.Collections;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.quora.challenge.graph.AdjacencyList;

public class BiConnectedPruningStrategyTest {

    private BiConnectedPruningStrategy<Integer> strategy;

    @BeforeTest
    public void setup() {
        strategy = new BiConnectedPruningStrategy<Integer>();
    }

    @Test
    public void isNotBiConnectedSimpleGraphTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 2);

        al.addEdge(1, 0);
        al.addEdge(1, 3);

        al.addEdge(2, 0);
        al.addEdge(2, 3);

        al.addEdge(3, 4);
        al.addEdge(4, 3);

        Set<Integer> currentPath = Collections.emptySet();
        Assert.assertFalse(strategy.isBiConnected(al, 0, currentPath, 4));
    }

    @Test
    public void isBiConnectedSimpleGraphTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        al.addEdge(0, 1);
        al.addEdge(0, 2);

        al.addEdge(1, 0);
        al.addEdge(1, 3);

        al.addEdge(2, 0);
        al.addEdge(2, 3);

        al.addEdge(3, 4);
        al.addEdge(4, 3);

        al.addEdge(4, 2);
        al.addEdge(2, 4);

        Set<Integer> currentPath = Collections.emptySet();
        Assert.assertTrue(strategy.isBiConnected(al, 0, currentPath, 4));
    }
}
