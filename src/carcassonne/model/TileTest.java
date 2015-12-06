package carcassonne.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

public class TileTest {
    public Tile tile;
    public Player player = new Player();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void initialize() {
        tile = new Tile();
    }

    @Test
    public void setXYgetXY() {
        tile.setCoordinates(0, 0);
        assertEquals("X coordinate", 0, tile.getX());
        assertEquals("Y coordinate", 0, tile.getY());
    }

    @Test
    public void coordinateConstructorTest() {
        Tile tile = new Tile(0, 0);
        assertEquals(0, tile.getX());
        assertEquals(0, tile.getY());
    }

    @Test
    public void ifGetCoordinatesNullThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.getCoordinates();
    }

    @Test
    public void hasCoordinates() {
        assertFalse(tile.hasCoordinates());
        tile.setCoordinates(0, 0);
        assertTrue(tile.hasCoordinates());
    }

    @Test
    public void coordinatesEqualsAndHashCodeTest() {
        Coordinates c1 = new Coordinates(1, 1);
        Coordinates c2 = new Coordinates(1, 1);
        assertTrue(c1.equals(c2));
        assertEquals(c1.hashCode(), c2.hashCode());
        Coordinates c3 = new Coordinates(2, 3);
        assertTrue(!c1.equals(c3));
        assertTrue(c1.hashCode() != c3.hashCode());
    }

    @Test
    public void ifFollowerNotPlacedThenNoFollowers() {
        assertEquals("No followers placed", true, tile.isNoFollower());
    }

    @Test
    public void ifFollowerPlacedThenNoFollowersFalse() {
        tile.placeFollower(player, new Feature());
        assertEquals("Follower is placed on tile", false, tile.isNoFollower());
    }

    @Test
    public void ifHorizontalRoadTileThenRoadConnectsEastAndWest() {
        tile.addFeature(new Feature(), TileDirections.EAST, TileDirections.WEST);
        assertTrue("Horizontal road is connected west to east",
                tile.getDestinations(TileDirections.WEST).contains(TileDirections.EAST));
        assertTrue("Horizontal road is connected east to west",
                tile.getDestinations(TileDirections.EAST).contains(TileDirections.WEST));
    }

    @Test
    public void crossroadsIsNodConnecting() {
        tile.addFeature(new Feature(), TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.SOUTH);

        assertTrue("Crossroad leads WEST to END",
                    tile.getDestinations(TileDirections.WEST).contains(TileDirections.END));
        assertFalse("Crossroad does not connect EAST and WEST",
                tile.getDestinations(TileDirections.WEST).contains(TileDirections.EAST));
    }

    @Test
    public void horizontalRoadTileHasConnectingLands() {
        /*
         * The road
         */
        tile.addFeature(new Feature(), TileDirections.EAST, TileDirections.WEST);
        /*
         * The land
         */
        tile.addFeature(new Feature(),TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);

        assertTrue("Land connects WWN and EEN",
                tile.getDestinations(TileDirections.WWN).contains(TileDirections.EEN));
    }

    @Test
    public void testCloister() {
        assertFalse("The tile doesn't have a cloister on it", tile.hasCloister());
        tile.addFeature(new Feature(), TileDirections.CENTER);
        assertTrue("The tile has a cloister on it", tile.hasCloister());
    }

    @Test
    public void cloisterShouldNotConnectAnything() {
        tile.addFeature(new Feature(), TileDirections.CENTER);
        assertEquals("Cloister connects itself to one TileDirection", 1, tile.getDestinations(TileDirections.CENTER).size());
        assertTrue("Cloister connects itself to itself",
                tile.getDestinations(TileDirections.CENTER).contains(TileDirections.CENTER));
    }

    @Test
    public void testTileCompletenessEmptyTileThenException() {
        assertFalse("Empty tile is not complete", tile.isComplete());
    }

    @Test
    public void completeTileNoCloisterThenFalse() {
        tile.addFeature(new Feature(), TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void completeTileThenTrue() {
        tile.addFeature(new Feature(), TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.CENTER);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoThenFalse() {
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.CENTER);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoCloisterThenFalse() {
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void propertyWithTileDirectionExistsWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionSameWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.NORTH);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunctionWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunction2WhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
    }

    @Test
    public void getPropertiesReturnsProperties() {
        HashSet<Feature> expected = new HashSet<>();
        Feature feature = new Feature();
         /*
         * The road
         */
        tile.addFeature(feature, TileDirections.EAST, TileDirections.WEST);
        expected.add(feature);
        /*
         * The land
         */
        feature = new Feature();
        tile.addFeature(feature, TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);
        expected.add(feature);


        assertTrue ("Tile returns set of properties", expected.equals(tile.getFeatures()));
    }

    @Test
    public void ifNoFollowerThenGetOccuoiedFeatureRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.getOccupiedFeature();
    }


    @Test
    public void followerIsPlacedOnASpecificProperty() {
        Feature feature = new Feature();
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.placeFollower(player, feature);
        assertEquals("Feature object that follower is placed on", feature, tile.getOccupiedFeature());
    }

    @Test
    /*
     * idea: returnFollower() returns follower to the player it belonged.
     * realization: players follower counter changes. Follower objects only exist when they are placed on tile
     */
    public void returnFollowerReturnsFollowerToThePlayer() {
        Feature feature = new Feature();
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.placeFollower(player, feature);
        assertEquals("Number of followers a player has", 6, player.getNumberOfFollowers());
        assertFalse("Tile has a follower", tile.isNoFollower());
        tile.returnFollower();
        assertTrue("Tile should not have follower", tile.isNoFollower());
        assertEquals("Number of followers a player has", 7, player.getNumberOfFollowers());
    }

    @Test
    public void returnFollowerWhenNoFollowerThenRuntimeException() {
        assertTrue("There are no followers", tile.isNoFollower());
        exception.expect(RuntimeException.class);
        tile.returnFollower();
    }

    @Test
    public void getOccupiedFeature() {
        Feature feature = new Feature();
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.placeFollower(player, feature);
        HashSet<TileDirections> directionsSet = new HashSet<>();
        directionsSet.add(TileDirections.WEST);
        assertTrue("Returns tileDirection set of follower containing feature", directionsSet.equals(tile.getOccupiedFeatureDirections()));
    }

    @Test
    public void placeFollowerOnTileDirectionNoFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.placeFollower(new Player(), TileDirections.WEST);
    }

    @Test
    public void placeFollowerOnDirectionCorrectTileDirection() {
        assertTrue("Tile has no followers", tile.isNoFollower());
        Feature feature = new Feature();
        tile.addFeature(feature, TileDirections.WEST);
        tile.placeFollower(new Player(), TileDirections.WEST);
        assertFalse("Tile has a follower", tile.isNoFollower());

        Set<TileDirections> expected = new HashSet<>();
        expected.add(TileDirections.WEST);
        assertTrue("TileDirection of feature with follower", expected.equals(tile.getOccupiedFeatureDirections()));
    }
}




















