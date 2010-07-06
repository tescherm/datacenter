package com.quora.challenge.command;

import java.util.List;

/**
 * Structure representing an input grid.
 * 
 * @author mattt
 */
public class InputGrid {

    private final int rows;
    private final int cols;
    private final List<String> grid;

    /**
     * @param rows
     *            the number of rows in the grid.
     * @param cols
     *            the number of columns in the grid.
     * @param grid
     *            the list of elements in the grid.
     */
    public InputGrid(final int rows, final int cols, final List<String> grid) {
        if (rows <= 0) {
            throw new IllegalArgumentException("The number of rows must be >= 1");
        }
        if (cols <= 0) {
            throw new IllegalArgumentException("The number of cols must be >= 1");
        }
        if (grid.size() != rows * cols) {
            throw new IllegalArgumentException(
                    "The number of elements in the grid must match the given row and column dimensions.");
        }

        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
    }

    /**
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return the list of elements in the grid
     */
    public List<String> getGrid() {
        return grid;
    }
}
