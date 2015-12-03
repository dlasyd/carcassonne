package carcassonne.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

public class TileTest {
    public Tile tile;

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
        assertEquals("No followers placed", true, tile.isNoFollowers());
    }

    @Test
    public void ifFollowerPlacedThenNoFollowersFalse() {
        tile.placeFollower();
        assertEquals("Follower is placed on tile", false, tile.isNoFollowers());
    }

    @Test
    public void ifHorizontalRoadTileThenRoadConnectsEastAndWest() {
        tile.addProperty(new Property(), TileDirections.EAST, TileDirections.WEST);
        assertTrue("Horizontal road is connected", Arrays.asList(tile.getDestinations(TileDirections.WEST)).contains(TileDirections.EAST));
        assertTrue("Horizontal road is connected", Arrays.asList(tile.getDestinations(TileDirections.EAST)).contains(TileDirections.WEST));
    }

    @Test
    public void crossroadsIsNodConnecting() {
        tile.addProperty(new Property(), TileDirections.EAST);
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.NORTH);
        tile.addProperty(new Property(), TileDirections.SOUTH);

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
        tile.addProperty(new Property(), TileDirections.EAST, TileDirections.WEST);
        /*
         * The land
         */
        tile.addProperty(new Property(),TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);

        assertTrue("Land connects WWN and EEN",
                Arrays.asList(tile.getDestinations(TileDirections.WWN)).contains(TileDirections.EEN));
    }

    @Test
    public void testCloister() {
        assertFalse("The tile doesn't have a cloister on it", tile.hasCloister());
        tile.addProperty(new Property(), TileDirections.CENTER);
        assertTrue("The tile has a cloister on it", tile.hasCloister());
    }

    @Test
    public void cloisterShouldNotConnectAnything() {
        tile.addProperty(new Property(), TileDirections.CENTER);
        assertEquals("Cloister connects itself to one TileDirection", 1, tile.getDestinations(TileDirections.CENTER).length);
        assertTrue("Cloister connects itself to itself",
                Arrays.asList(tile.getDestinations(TileDirections.CENTER)).contains(TileDirections.CENTER));
    }

    @Test
    public void testTileCompletenessEmptyTileThenException() {
        assertFalse("Empty tile is not complete", tile.isComplete());
    }

    @Test
    public void completeTileNoCloisterThenNoException() {
        tile.addProperty(new Property(), TileDirections.EAST);
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.SOUTH);
        tile.addProperty(new Property(), TileDirections.NORTH);
        tile.addProperty(new Property(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Property(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Property(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Property(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void completeTileThenNoException() {
        tile.addProperty(new Property(), TileDirections.EAST);
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.SOUTH);
        tile.addProperty(new Property(), TileDirections.NORTH);
        tile.addProperty(new Property(), TileDirections.CENTER);
        tile.addProperty(new Property(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Property(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Property(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Property(), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoThenNoException() {
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.SOUTH);
        tile.addProperty(new Property(), TileDirections.NORTH);
        tile.addProperty(new Property(), TileDirections.CENTER);
        tile.addProperty(new Property(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Property(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Property(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Property(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoCloisterThenNoException() {
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.SOUTH);
        tile.addProperty(new Property(), TileDirections.NORTH);
        tile.addProperty(new Property(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Property(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Property(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Property(), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void propertyWithTileDirectionExistsWhenAddindThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addProperty(new Property(), TileDirections.WEST);
        tile.addProperty(new Property(), TileDirections.WEST);
    }

}




















