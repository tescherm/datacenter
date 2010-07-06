package com.quora.challenge.graph.ham;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.quora.challenge.PathStatistics;
import com.quora.challenge.RoomGraphFactory;
import com.quora.challenge.TestUtils;
import com.quora.challenge.domain.Room;
import com.quora.challenge.graph.Graph;

public class HamiltonianPathCounterTest {

    private HamiltonianPathCounter<Room> pathCounter;

    @BeforeClass
    public void setup() {
        pathCounter = HamiltonianPathCounterFactory.getInstance();
    }

    @Test
    public void simplePathTest() {
        final Graph<Room> graph = RoomGraphFactory.generateRoomGraph(1, 2, Lists.newArrayList("2", "3"));
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 1);
    }

    @Test
    public void quoraExamplePathTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("quora-example-path-2-paths");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 2);
    }

    @Test
    public void adjacentStartGoal1PathTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("adjacent-start-goal-1-path");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 1);
    }

    @Test
    public void adjacentStartGoalNoPathTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("adjacent-start-goal-no-path");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 0);
    }

    @Test
    public void sevenCols4Rows38PathsTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("7-cols-4-rows-38-paths");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 38);
    }

    @Test
    public void sixCols6Rows1770PathsTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("6-cols-6-rows-1770-paths");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 1770);
    }

    @Test
    public void sixCols6RowsNoPathsTest() {
        final Graph<Room> graph = TestUtils.getRoomGraphFromFixture("6-cols-6-rows-no-path");
        final PathStatistics<Room> stats = pathCounter.findPaths(graph);
        Assert.assertEquals(stats.getPathCount(), 0);
    }

}
