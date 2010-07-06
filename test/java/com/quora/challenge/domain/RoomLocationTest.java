package com.quora.challenge.domain;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

public class RoomLocationTest {

    @Test
    public void roomLocationEqualityTest() {
        Room.RoomLocation r1 = new Room.RoomLocation(0, 0);
        Room.RoomLocation r2 = new Room.RoomLocation(0, 0);
        Assert.assertEquals(r1, r2);

        // ensure equal hash codes
        Set<Room.RoomLocation> rooms = new ImmutableSet.Builder<Room.RoomLocation>().add(r1).add(r2).build();
        Assert.assertEquals(rooms.size(), 1);

    }

    @Test
    public void roomLocationInequalityTest() {
        Room.RoomLocation r1 = new Room.RoomLocation(0, 1);
        Room.RoomLocation r2 = new Room.RoomLocation(0, 0);
        Assert.assertNotSame(r1, r2);

        // ensure unequal hash codes
        Set<Room.RoomLocation> rooms = new ImmutableSet.Builder<Room.RoomLocation>().add(r1).add(r2).build();
        Assert.assertEquals(rooms.size(), 2);

    }
}
