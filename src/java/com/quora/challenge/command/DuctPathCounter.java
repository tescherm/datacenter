package com.quora.challenge.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.quora.challenge.PathStatistics;
import com.quora.challenge.RoomGraphFactory;
import com.quora.challenge.domain.Room;
import com.quora.challenge.graph.Graph;
import com.quora.challenge.graph.ham.HamiltonianPathCounter;
import com.quora.challenge.graph.ham.HamiltonianPathCounterFactory;

/**
 * Entry point for the duct counter.
 * 
 * @author mattt
 */
public class DuctPathCounter {

    /**
     * @param args
     * @throws IOException
     *             if an error occurs while reading grid input.
     */
    public static void main(String[] args) throws IOException {

        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a grid in the form 'cols rows room1 room2 ... roomN' :");
        final String input = in.readLine();

        final Graph<Room> roomGraph = RoomGraphFactory.generateRoomGraph(InputGridUtil.asInputGrid(input));
        final HamiltonianPathCounter<Room> pathCounter = HamiltonianPathCounterFactory.getInstance();

        System.out.println("Finding paths for input '" + input + "' ...");
        System.out.println();
        final PathStatistics<Room> statistics = pathCounter.findPaths(roomGraph);
        System.out.println(statistics.getPathCount());
    }

}
