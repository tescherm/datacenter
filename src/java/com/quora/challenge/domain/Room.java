package com.quora.challenge.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Describes a room.
 * 
 * @author mattt
 */
public class Room {

    private final Type type;
    private final RoomLocation location;

    /**
     * @param type
     *            is the {@link Type} associated with this room.
     * @param location
     *            is the location (that is, the row and column coordinate) associated with this room.
     */
    public Room(final Type type, final RoomLocation location) {
        this.type = type;
        this.location = location;
    }

    /**
     * @return the room type.
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the room location
     */
    public RoomLocation getLocation() {
        return location;
    }

    /**
     * An enum describing available room types.
     * 
     * @author mattt
     */
    public static enum Type {
        OWNED("0", "Owned"),
        NOT_OWNED("1", "Not owned"), 
        START("2", "Start"), 
        GOAL("3", "Goal");

        private final String roomId;
        private final String label;

        /**
         * @param roomId
         *            is the unique id for the room type
         * @param label
         *            is a human readable descriptor for the room type.
         */
        private Type(final String roomId, final String label) {
            this.roomId = roomId;
            this.label = label;
        }

        /**
         * @return the room id.
         */
        public String getRoomId() {
            return roomId;
        }

        /**
         * @return the room label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Obtains the room {@link Type} for the room with the given id.
         * 
         * @param roomId
         *            is the room id to obtain the {@link Type} for.
         * @return the room {@link Type} corresponding to the given room id.
         */
        public static Type getType(final String roomId) {
            // I didn't want to use the enum ordinal (i.e. Type.OWNED.ordinal())
            // since it might differ from the expected
            // value if different rooms types are added in the future.
            final List<Type> values = Arrays.asList(values());
            for (final Type type : values) {
                if (type.getRoomId().equals(roomId)) {
                    return type;
                }
            }
            throw new IllegalStateException("Unexpected room id: " + roomId);
        }
    }

    /**
     * Describes a room location.
     * 
     * @author mattt
     */
    public static final class RoomLocation {

        private final Integer x;
        private final Integer y;

        /**
         * @param x
         *            is the x position for the room
         * @param y
         *            is the y position for the room.
         */
        public RoomLocation(Integer x, Integer y) {
            this.x = x;
            this.y = y;

        }

        /**
         * @return the x position for the room.
         */
        public Integer getX() {
            return x;
        }

        /**
         * @return the y position for the room.
         */
        public Integer getY() {
            return y;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((x == null) ? 0 : x.hashCode());
            result = prime * result + ((y == null) ? 0 : y.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            RoomLocation other = (RoomLocation) obj;
            if (x == null) {
                if (other.x != null)
                    return false;
            } else if (!x.equals(other.x))
                return false;
            if (y == null) {
                if (other.y != null)
                    return false;
            } else if (!y.equals(other.y))
                return false;
            return true;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "(x=" + x + ", y=" + y + ")";
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + type.getLabel() + " " + location + "]";
    }
}
