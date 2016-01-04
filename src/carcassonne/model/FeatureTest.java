package carcassonne.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 03/12/15.
 */
public class FeatureTest {
    Feature feature = Feature.createFeature(FeatureType.CITY);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isFollowerPlacedInBeginning() {
        assertEquals ("Follower not placed on feature", false, feature.isFollowerPlaced());
    }

    @Test
    public void followerPlacedThenTrue() {
        feature.setFollower(new Follower(new Player()));
        assertEquals ("Follower placed on feature", true, feature.isFollowerPlaced());
    }

    @Test
    public void placeFollowerIfPlacedThenRuntimeException() {
        exception.expect(RuntimeException.class);
        feature.setFollower(new Follower(new Player()));
        feature.setFollower(new Follower(new Player()));
    }
}