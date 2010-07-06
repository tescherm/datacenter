package com.quora.challenge;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.quora.challenge.domain.Room;
import com.quora.challenge.graph.AdjacencyList;
import com.quora.challenge.graph.Graph;

public class RoomGraphFactoryTest {

    @Test
    public void roomGraphGenerationTest() {
        Graph<Room> roomGraph = RoomGraphFactory.generateRoomGraph(2, 2, Lists.newArrayList("2", "0", "0", "3"));

        Room goalRoom = new Room(Room.Type.GOAL, new Room.RoomLocation(1, 1));
        Room startRoom = roomGraph.getStart();
        Assert.assertEquals(startRoom, new Room(Room.Type.START, new Room.RoomLocation(0, 0)));

        AdjacencyList<Room> graph = roomGraph.getGraph();
        Assert.assertEquals(graph.vertexCount(), 4);

        Collection<Room> verts = graph.getVerticesFromSource(startRoom);
        Assert.assertEquals(verts.size(), 2);

        for (Room room : verts) {
            final Collection<Room> subVerts = graph.getVerticesFromSource(room);
            Assert.assertEquals(verts.size(), 2);
            Assert.assertTrue(subVerts.contains(goalRoom));
        }
    }
}
