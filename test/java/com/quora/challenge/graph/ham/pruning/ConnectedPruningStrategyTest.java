package com.quora.challenge.graph.ham.pruning;

import java.util.Collections;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.quora.challenge.graph.AdjacencyList;

public class ConnectedPruningStrategyTest {

    private ConnectedPruningStrategy<Integer> connectedTester;

    @BeforeClass
    public void setup() {
        connectedTester = new ConnectedPruningStrategy<Integer>();
    }

    @Test
    public void isNotConnectedSimpleTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        // in this graph vertex 4 will break the unexplored verticies into
        // disjoint sets (hence they are no longer connected)F
        al.addEdge(0, 4);

        al.addEdge(1, 2);
        al.addEdge(1, 3);
        al.addEdge(1, 4);

        al.addEdge(2, 1);
        al.addEdge(2, 3);

        al.addEdge(3, 1);
        al.addEdge(3, 2);

        al.addEdge(4, 0);
        al.addEdge(4, 1);
        al.addEdge(4, 5);

        Set<Integer> currentPath = Collections.singleton(0);
        Assert.assertFalse(connectedTester.isConnected(currentPath, al, 4));

    }

    @Test
    public void isConnectedSimpleTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();

        // this graph is identical to the one in isConnectedSimpleTest() except
        // for an undirected edge between 3 and 5.
        al.addEdge(0, 4);

        al.addEdge(1, 2);
        al.addEdge(1, 3);
        al.addEdge(1, 4);

        al.addEdge(2, 1);
        al.addEdge(2, 3);

        al.addEdge(3, 1);
        al.addEdge(3, 2);

        al.addEdge(4, 0);
        al.addEdge(4, 1);
        al.addEdge(4, 5);

        // added edge
        al.addEdge(3, 5);
        al.addEdge(5, 3);

        Set<Integer> currentPath = Collections.singleton(0);
        Assert.assertTrue(connectedTester.isConnected(currentPath, al, 4));
    }

    @Test
    public void isNotConnectedThreePathsTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();
        al.addEdge(0, 1);
        al.addEdge(0, 2);
        al.addEdge(0, 3);

        al.addEdge(3, 0);
        al.addEdge(2, 0);
        al.addEdge(1, 0);

        Set<Integer> currentPath = Collections.emptySet();
        Assert.assertFalse(connectedTester.isConnected(currentPath, al, 0));

    }

    @Test
    public void isConnectedThreePathsTest() {
        AdjacencyList<Integer> al = new AdjacencyList<Integer>();
        al.addEdge(0, 1);
        al.addEdge(0, 2);
        al.addEdge(0, 3);

        al.addEdge(3, 0);
        al.addEdge(2, 0);
        al.addEdge(1, 0);

        al.addEdge(3, 2);
        al.addEdge(2, 3);

        al.addEdge(1, 2);
        al.addEdge(2, 1);

        Set<Integer> currentPath = Collections.emptySet();
        Assert.assertTrue(connectedTester.isConnected(currentPath, al, 0));

    }
}
