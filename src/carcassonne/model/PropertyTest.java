package carcassonne.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 03/12/15.
 */
public class PropertyTest {
    Property property = new Property();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isFollowerPlacedInBeginning() {
        assertEquals ("Follower not placed on property", false, property.isFollowerPlaced());
    }

    @Test
    public void followerPlacedThenTrue() {
        property.setFollower(new Follower(new Player()));
        assertEquals ("Follower placed on property", true, property.isFollowerPlaced());
    }

    @Test
    public void placeFollowerIfPlacedThenRuntimeException() {
        exception.expect(RuntimeException.class);
        property.setFollower(new Follower(new Player()));
        property.setFollower(new Follower(new Player()));
    }
}