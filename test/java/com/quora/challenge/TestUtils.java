package com.quora.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.testng.Assert;

import com.quora.challenge.command.InputGridUtil;
import com.quora.challenge.domain.Room;
import com.quora.challenge.graph.Graph;

/**
 * Util methods related to testing.
 * 
 * @author mattt
 */
public final class TestUtils {

    /**
     * suppress default constructor
     */
    private TestUtils() {
        throw new AssertionError();
    }

    /**
     * Obtain the {@link Room} {@link Graph} for the given test fixture.
     * 
     * @param fixture
     *            is the test fixture to get the room graph for.
     * @return the {@link Room} {@link Graph}
     */
    public static Graph<Room> getRoomGraphFromFixture(final String fixture) {
        final InputStream is = TestUtils.class.getResourceAsStream("/fixtures/" + fixture);
        assert is != null;

        String input = null;
        try {
            input = TestUtils.convertStreamToString(is);
        } catch (IOException e) {
            // since this only runs for tests we'll fail the test if an io
            // exception occurs
            Assert.fail();
        }
        return RoomGraphFactory.generateRoomGraph(InputGridUtil.asInputGrid(input));
    }

    // TODO ultimately this would go in some util class
    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

}
