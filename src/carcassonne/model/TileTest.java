package carcassonne.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashSet;

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
        tile.addProperty(new Feature(), TileDirections.EAST, TileDirections.WEST);
        assertTrue("Horizontal road is connected", Arrays.asList(tile.getDestinations(TileDirections.WEST)).contains(TileDirections.EAST));
        assertTrue("Horizontal road is connected", Arrays.asList(tile.getDestinations(TileDirections.EAST)).contains(TileDirections.WEST));
    }

    @Test
    public void crossroadsIsNodConnecting() {
        tile.addProperty(new Feature(), TileDirections.EAST);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.SOUTH);

        assertTrue("Crossroad leads WEST to END",
                    Arrays.asList(tile.getDestinations(TileDirections.WEST)).contains(TileDirections.END));
        assertFalse("Crossroad does not connect EAST and WEST",
                Arrays.asList(tile.getDestinations(TileDirections.WEST)).contains(TileDirections.EAST));
    }

    @Test
    public void horizontalRoadTileHasConnectingLands() {
        /*
         * The road
         */
        tile.addProperty(new Feature(), TileDirections.EAST, TileDirections.WEST);
        /*
         * The land
         */
        tile.addProperty(new Feature(),TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);

        assertTrue("Land connects WWN and EEN",
                Arrays.asList(tile.getDestinations(TileDirections.WWN)).contains(TileDirections.EEN));
    }

    @Test
    public void testCloister() {
        assertFalse("The tile doesn't have a cloister on it", tile.hasCloister());
        tile.addProperty(new Feature(), TileDirections.CENTER);
        assertTrue("The tile has a cloister on it", tile.hasCloister());
    }

    @Test
    public void cloisterShouldNotConnectAnything() {
        tile.addProperty(new Feature(), TileDirections.CENTER);
        assertEquals("Cloister connects itself to one TileDirection", 1, tile.getDestinations(TileDirections.CENTER).length);
        assertTrue("Cloister connects itself to itself",
                Arrays.asList(tile.getDestinations(TileDirections.CENTER)).contains(TileDirections.CENTER));
    }

    @Test
    public void testTileCompletenessEmptyTileThenException() {
        assertFalse("Empty tile is not complete", tile.isComplete());
    }

    @Test
    public void completeTileNoCloisterThenFalse() {
        tile.addProperty(new Feature(), TileDirections.EAST);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void completeTileThenTrue() {
        tile.addProperty(new Feature(), TileDirections.EAST);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoThenFalse() {
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoCloisterThenFalse() {
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void propertyWithTileDirectionExistsWhenAddindThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.WEST);
    }

    @Test
    public void getPropertiesReturnsProperties() {
        HashSet<Feature> expected = new HashSet<>();
        Feature feature = new Feature();
         /*
         * The road
         */
        tile.addProperty(feature, TileDirections.EAST, TileDirections.WEST);
        expected.add(feature);
        /*
         * The land
         */
        feature = new Feature();
        tile.addProperty(feature, TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);
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
        tile.addProperty(feature, TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
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
        tile.addProperty(feature, TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
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
        tile.addProperty(feature, TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.placeFollower(player, feature);
        HashSet<TileDirections> directionsSet = new HashSet<>();
        directionsSet.add(TileDirections.WEST);
        System.out.println(directionsSet);
        System.out.println(tile.getOccupiedFeatureDirections());
        assertTrue("Returns tileDirection set of follower containing feature", directionsSet.equals(tile.getOccupiedFeatureDirections()));
    }
}




















