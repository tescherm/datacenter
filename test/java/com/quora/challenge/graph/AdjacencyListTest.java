package com.quora.challenge.graph;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AdjacencyListTest {

    @Test
    public void newAdjacencyListInstanceTest() {
        AdjacencyList<String> al = new AdjacencyList<String>();

        final String src1 = "src1";
        final String dest1 = "dest1";

        al.addEdge(src1, dest1);

        AdjacencyList<String> newAl = AdjacencyList.newInstance(al);

        al.removeEdge(src1, dest1);
        Assert.assertEquals(al.getVerticesFromSource(src1).size(), 0);
        Assert.assertEquals(newAl.getVerticesFromSource(src1).size(), 1);
    }

    @Test
    public void testAddEdgeManipulations() {

        AdjacencyList<String> al = new AdjacencyList<String>();

        final String src1 = "src1";
        final String dest1 = "dest1";

        Assert.assertFalse(al.containsEdge(src1, dest1));
        al.addEdge(src1, dest1);
        Assert.assertTrue(al.containsEdge(src1, dest1));

        final String src2 = "src2";

        al.addVertex(src2);
        al.addVertex(dest1);

        Assert.assertFalse(al.containsEdge(src2, dest1));
        al.addEdge(src2, dest1);
        Assert.assertTrue(al.containsEdge(src2, dest1));

    }

    @Test
    public void removeVertexTest() {
        AdjacencyList<Integer> list = new AdjacencyList<Integer>();

        list.addEdge(0, 1);
        list.addEdge(1, 0);
        list.addEdge(1, 2);
        list.addEdge(2, 1);

        list.removeVertex(0);

        Assert.assertEquals(list.vertexCount(), 2);
        Assert.assertTrue(list.getVerticesFromSource(0).isEmpty());

        Assert.assertEquals(list.getVerticesFromSource(1).size(), 1);

    }

    @Test(enabled = true)
    public void systemWalkerTest() {
        AdjacencyList<Integer> list = new AdjacencyList<Integer>();

        final int root = 0;
        final int c1Start = 1;
        final int c2Start = 2;
        final int c3Start = 3;
        final int c4Start = 4;
        final int con1 = 5;
        final int con2 = 6;

        list.addEdge(root, c1Start);
        list.addEdge(root, c2Start);
        list.addEdge(root, c3Start);
        list.addEdge(root, c4Start);
        list.addEdge(c1Start, con1);
        list.addEdge(c2Start, con1);
        list.addEdge(c3Start, con2);
        list.addEdge(c4Start, con2);

        walkIntegerList(list, root);
    }

    @Test(enabled = true)
    public void closedDepSystemWalkerTest() {
        AdjacencyList<Integer> list = new AdjacencyList<Integer>();

        final int root = 0;
        final int c1Start = 1;
        final int c2Start = 2;
        final int c3Start = 3;
        final int c4Start = 4;
        final int con1 = 5;
        final int con2 = 6;
        final int conFinal = 7;

        list.addEdge(root, c1Start);
        list.addEdge(root, c2Start);
        list.addEdge(root, c3Start);
        list.addEdge(root, c4Start);
        list.addEdge(c1Start, con1);
        list.addEdge(c2Start, con1);
        list.addEdge(c3Start, con2);
        list.addEdge(c4Start, con2);
        list.addEdge(con1, conFinal);
        list.addEdge(con2, conFinal);

        Assert.assertEquals(walkIntegerList(list, root).size(), 8);
    }

    @Test(enabled = true)
    public void parallelWalkerTest() {
        AdjacencyList<Integer> list = new AdjacencyList<Integer>();

        final int root = 0;
        final int c1Start = 1;
        final int c2Start = 2;
        final int c3Start = 3;
        final int c4Start = 4;

        list.addEdge(root, c1Start);
        list.addEdge(root, c2Start);
        list.addEdge(root, c3Start);
        list.addEdge(root, c4Start);

        Assert.assertEquals(walkIntegerList(list, root).size(), 5);
    }

    @Test(enabled = true)
    public void serialWalkerTest() {
        AdjacencyList<Integer> list = new AdjacencyList<Integer>();

        final int root = 0;
        final int c1Start = 1;
        final int c2Start = 2;
        final int c3Start = 3;
        final int c4Start = 4;

        list.addEdge(root, c1Start);
        list.addEdge(c1Start, c2Start);
        list.addEdge(c2Start, c3Start);
        list.addEdge(c3Start, c4Start);

        Assert.assertEquals(walkIntegerList(list, root).size(), 5);
    }

    private static Set<Integer> walkIntegerList(AdjacencyList<Integer> list, Integer vert) {
        Set<Integer> visited = new HashSet<Integer>();
        visited.add(vert);

        for (Integer adj : list.getVerticesFromSource(vert)) {
            walk(visited, list, adj);
        }
        return visited;
    }

    private static void walk(Set<Integer> visited, AdjacencyList<Integer> list, Integer vert) {
        if (visited.contains(vert)) {
            return;
        }
        visited.add(vert);
        for (Integer adj : list.getVerticesFromSource(vert)) {
            walk(visited, list, adj);
        }
    }

}
