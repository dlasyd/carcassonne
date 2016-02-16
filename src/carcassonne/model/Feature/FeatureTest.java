package carcassonne.model.feature;

import carcassonne.model.tile.Follower;
import carcassonne.model.Player;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class FeatureTest {
    private final Feature feature = Feature.createFeature(FeatureType.CITY);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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