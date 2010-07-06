package com.quora.challenge.domain;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.quora.challenge.domain.Room.Type;

public class RoomTest {

    @Test
    public void roomEqualityTest() {
        Room r1 = new Room(Room.Type.OWNED, new Room.RoomLocation(0, 0));
        Room r2 = new Room(Room.Type.OWNED, new Room.RoomLocation(0, 0));
        Assert.assertEquals(r1, r2);

        // ensure equal hash codes
        Set<Room> rooms = new ImmutableSet.Builder<Room>().add(r1).add(r2).build();
        Assert.assertEquals(rooms.size(), 1);
    }

    @Test
    public void roomInequalityTest() {
        Room r1 = new Room(Room.Type.OWNED, new Room.RoomLocation(0, 1));
        Room r2 = new Room(Room.Type.START, new Room.RoomLocation(0, 0));
        Assert.assertNotSame(r1, r2);

        // ensure unequal hash codes
        Set<Room> rooms = new ImmutableSet.Builder<Room>().add(r1).add(r2).build();
        Assert.assertEquals(rooms.size(), 2);
    }

    @Test
    public void getTypeTest() {
        Type[] types = Room.Type.values();
        for (Type type : types) {
            Type roomType = Room.Type.getType(type.getRoomId());
            Assert.assertEquals(roomType, type);
        }
    }
}
