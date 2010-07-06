package com.quora.challenge.command;

import java.util.Arrays;

/**
 * A utility to process an input grid.
 * 
 * @author mattt
 */
public final class InputGridUtil {

    /**
     * suppress default constructor
     */
    private InputGridUtil() {
        throw new AssertionError();
    }

    /**
     * Converts the given input {@link String} to an {@link InputGrid} representation.
     * 
     * @param input
     *            is the input to convert.
     * @return an {@link InputGrid} representation of the given input string.
     */
    public static InputGrid asInputGrid(String input) {
        if (input == null)
            throw new NullPointerException();
        input = input.replaceAll("\\\n", "");
        final String[] split = input.split(" ");

        // at a minimum the input must contain a row, column, start and a goal
        assert split.length >= 4;

        final int cols = Integer.parseInt(split[0]);
        final int rows = Integer.parseInt(split[1]);

        final String[] grid = Arrays.copyOfRange(split, 2, split.length);

        return new InputGrid(rows, cols, Arrays.asList(grid));
    }
}
