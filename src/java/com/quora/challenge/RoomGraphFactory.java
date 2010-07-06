package com.quora.challenge;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.quora.challenge.command.InputGrid;
import com.quora.challenge.domain.Room;
import com.quora.challenge.graph.AdjacencyList;
import com.quora.challenge.graph.Graph;

/**
 * Factory for generating a {@link Room} graph using a given room grid.
 * 
 * @author mattt
 */
public final class RoomGraphFactory {

    /**
     * suppress default constructor
     */
    private RoomGraphFactory() {
        throw new AssertionError();
    }

    /**
     * Constructs a {@link Room} {@link Graph} for a given input grid.
     * 
     * @param inputGrid
     *            is the {@link InputGrid} to generate the room graph for. The number of rooms in the grid is expected
     *            to = rows*cols.
     * @return a {@link Graph} consisting of {@link Room} objects for the given grid.
     */
    public static Graph<Room> generateRoomGraph(final InputGrid inputGrid) {
        return generateRoomGraph(inputGrid.getRows(), inputGrid.getCols(), inputGrid.getGrid());
    }

    /**
     * Constructs a {@link Room} {@link Graph} for a given grid, with the given number of rows and columns.
     * 
     * @param rows
     *            is the number of rows in the grid. The number of rows is expected to be >= 1.
     * @param cols
     *            is the number of columns in the grid. The number of columns is expected to be >= 1.
     * @param grid
     *            is the grid with exactly one start and one goal room. The number of rooms in the grid is expected to
     *            equal rows*cols.
     * @return a {@link Graph} consisting of {@link Room} objects for the given row, column, and grid.
     */
    public static Graph<Room> generateRoomGraph(final int rows, final int cols, final List<String> grid) {

        assert rows > 0;
        assert cols > 0;
        assert grid.size() == rows * cols;

        final AdjacencyList<Room> graph = new AdjacencyList<Room>();

        int colPos = 0;
        int rowPos = 0;

        // convert the grid (currently an list of strings) to a list of Room
        // enum types
        final List<Room.Type> typeGrid = Lists.transform(grid, new Function<String, Room.Type>() {
            public Room.Type apply(final String room) {
                return Room.Type.getType(room);
            }
        });

        int currentIndex = 0;
        Room startRoom = null;
        Room goalRoom = null;

        // construct the adjacency list off of the grid
        for (final Iterator<Room.Type> iterator = typeGrid.iterator(); iterator.hasNext(); currentIndex++) {

            final Room.Type currentType = iterator.next();

            switch (currentType) {
            case NOT_OWNED:
                rowPos = incrementNextRoomRow(rowPos, colPos, cols);
                colPos = incrementNextRoomCol(colPos, cols);
                break;
            case START:
                startRoom = new Room(currentType, new Room.RoomLocation(rowPos, colPos));
            case GOAL:
                goalRoom = new Room(currentType, new Room.RoomLocation(rowPos, colPos));
            case OWNED:
                final Room currentRoom = new Room(currentType, new Room.RoomLocation(rowPos, colPos));

                addAdjacentEdge(currentRoom, getNorthRoom(currentIndex, cols, rowPos, colPos, typeGrid), graph);
                addAdjacentEdge(currentRoom, getSouthRoom(currentIndex, cols, rows, rowPos, colPos, typeGrid), graph);
                addAdjacentEdge(currentRoom, getEastRoom(currentIndex, cols, rows, rowPos, colPos, typeGrid), graph);
                addAdjacentEdge(currentRoom, getWestRoom(currentIndex, rowPos, colPos, typeGrid), graph);

                rowPos = incrementNextRoomRow(rowPos, colPos, cols);
                colPos = incrementNextRoomCol(colPos, cols);
                break;
            default:
                throw new IllegalStateException("Unexpected room type obtained: " + currentType);
            }
        }

        assert graph.vertexCount() <= grid.size();

        return new Graph<Room>(startRoom, goalRoom, graph);
    }

    /**
     * Increments the next room row based on the given row position, column position and number of columns in the grid.
     * 
     * @param rowPos
     *            is the current row position.
     * @param colPos
     *            is the current column position
     * @param cols
     *            is the number of columns in the grid
     * @return the updated row position.
     */
    private static int incrementNextRoomRow(int rowPos, int colPos, int cols) {
        return colPos == cols - 1 ? ++rowPos : rowPos;
    }

    /**
     * Increments the next room column based on the given column position and number of columns in the grid.
     * 
     * @param colPos
     *            is the current column position
     * @param cols
     *            is the number of columns in the grid
     * @return the updated column position.
     */
    private static int incrementNextRoomCol(int colPos, int cols) {
        return colPos == cols - 1 ? 0 : ++colPos;
    }

    /**
     * Adds the given adjacent room to the given current room in the given graph.
     * 
     * @param currentRoom
     *            is the current (i.e. source) room
     * @param adjacentRoom
     *            is the adjacent (i.e. destination) room to add an edge to.
     * @param graph
     *            is the graph to add the edge in.
     */
    private static void addAdjacentEdge(final Room currentRoom, final Room adjacentRoom, final AdjacencyList<Room> graph) {
        if (adjacentRoom != null) {
            graph.addEdge(currentRoom, adjacentRoom);
        }
    }

    /**
     * Obtains the room north of the current room (that is, the room identified by the given index). If no adjacent
     * north room exists, or the room is a sink room (see <code>isSinkRoom(Room.Type)</code>), <code>null</code> will be
     * returned.
     * 
     * @param index
     *            is the index of the current room in the given room grid.
     * @param cols
     *            is the number of columns in the grid.
     * @param rowPos
     *            is the row position of the current room.
     * @param colPos
     *            is the column position of the current room.
     * @param typeGrid
     *            is the {@link Type} grid to obtain the {@link Room} type from.
     * @return the room north of the current room, or <code>null</code> if the room is a sink room, or no northern room
     *         exists.
     */
    private static Room getNorthRoom(final int index, final int cols, final int rowPos, final int colPos,
            final List<Room.Type> typeGrid) {
        if (index - cols >= 0) {
            final Room.Type roomType = typeGrid.get(index - cols);
            return !isSinkRoom(roomType) ? new Room(roomType, new Room.RoomLocation(rowPos - 1, colPos)) : null;
        }
        return null;
    }

    /**
     * Obtains the room south of the current room (that is, the room identified by the given index). If no adjacent
     * south room exists, or the room is a sink room (see <code>isSinkRoom(Room.Type)</code>), <code>null</code> will be
     * returned.
     * 
     * @param index
     *            is the index of the current room in the given room grid.
     * @param cols
     *            is the number of columns in the grid.
     * @param rows
     *            is the number of rows in the grid.
     * @param rowPos
     *            is the row position of the current room.
     * @param colPos
     *            is the column position of the current room.
     * @param typeGrid
     *            is the {@link Type} grid to obtain the {@link Room} type from.
     * @return the room south of the current room, or <code>null</code> if the room is a sink room, or no southern room
     *         exists.
     */
    private static Room getSouthRoom(final int index, final int cols, final int rows, final int rowPos,
            final int colPos, final List<Room.Type> typeGrid) {
        if (index + cols < rows * cols) {
            final Room.Type roomType = typeGrid.get(index + cols);
            return !isSinkRoom(roomType) ? new Room(roomType, new Room.RoomLocation(rowPos + 1, colPos)) : null;
        }
        return null;
    }

    /**
     * Obtains the room east of the current room (that is, the room identified by the given index). If no adjacent east
     * room exists, or the room is a sink room (see <code>isSinkRoom(Room.Type)</code>), <code>null</code> will be
     * returned.
     * 
     * @param index
     *            is the index of the current room in the given room grid.
     * @param cols
     *            is the number of columns in the grid.
     * @param rows
     *            is the number of rows in the grid.
     * @param rowPos
     *            is the row position of the current room.
     * @param colPos
     *            is the column position of the current room.
     * @param typeGrid
     *            is the {@link Type} grid to obtain the {@link Room} type from.
     * @return the room south of the current room, or <code>null</code> if the room is a sink room, or no eastern room
     *         exists.
     */
    private static Room getEastRoom(final int index, final int cols, final int rows, final int rowPos,
            final int colPos, final List<Room.Type> typeGrid) {
        if (colPos != cols - 1 && index < rows * cols - 1) {
            final Room.Type roomType = typeGrid.get(index + 1);
            return !isSinkRoom(roomType) ? new Room(roomType, new Room.RoomLocation(rowPos, colPos + 1)) : null;
        }
        return null;
    }

    /**
     * Obtains the room west of the current room (that is, the room identified by the given index). If no adjacent west
     * room exists, or the room is a sink room (see <code>isSinkRoom(Room.Type)</code>), <code>null</code> will be
     * returned.
     * 
     * @param index
     *            is the index of the current room in the given room grid.
     * @param cols
     *            is the number of columns in the grid.
     * @param rows
     *            is the number of rows in the grid.
     * @param rowPos
     *            is the row position of the current room.
     * @param colPos
     *            is the column position of the current room.
     * @param typeGrid
     *            is the {@link Type} grid to obtain the {@link Room} type from.
     * @return the room south of the current room, or <code>null</code> if the room is a sink room, or no western room
     *         exists.
     */
    private static Room getWestRoom(final int index, final int rowPos, final int colPos, final List<Room.Type> typeGrid) {
        if (colPos != 0) {
            final Room.Type roomType = typeGrid.get(index - 1);
            return !isSinkRoom(roomType) ? new Room(roomType, new Room.RoomLocation(rowPos, colPos - 1)) : null;
        }
        return null;
    }

    /**
     * Determines if the given room type is a sink room for this graph. A sink room is a room with an outdegree of zero.
     * 
     * @param roomType
     * @return
     */
    private static boolean isSinkRoom(final Room.Type roomType) {
        return roomType.equals(Room.Type.NOT_OWNED);
    }
}
