package carcassonne.model;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PlayerTest {
    Player anton;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void initialize() {
        anton = new Player("Anton", Color.RED);
    }

    @Test
    public void createPlayerAndGetValues() {
        Player anton = new Player("Anton", Color.RED);
        assertEquals ("Anton", anton.getName());
        assertEquals (Color.RED, anton.getColor());
    }

    @Test
    public void addingAndRemovingFollowers() {
        assertEquals (7, anton.getNumberOfFollowers());
        anton.placeFollower();
        assertEquals (6, anton.getNumberOfFollowers());
        anton.returnFollower();
        assertEquals (7, anton.getNumberOfFollowers());
    }

    @Test
    public void ifFollowersBelow0ThenRuntimeException() {
        exception.expect(RuntimeException.class);
        for (int i = 0; i < 8; i++)
            anton.placeFollower();
    }

    @Test
    public void ifFollowersAbove7ThenRuntimeException() {
        exception.expect(RuntimeException.class);
        anton.returnFollower();
    }

    @Test
    public void noAvaliableFollowers() {
        for (int i = 0; i < 7; i++) {
            assertEquals ("Has follower", true, anton.hasAvailableFollowers());
            anton.placeFollower();
        }
        assertEquals ("Has follower", false, anton.hasAvailableFollowers());
    }

    /*
     * Testing of changing number of followers based on real estate events are in RealEstateManagerTest
     */
}
