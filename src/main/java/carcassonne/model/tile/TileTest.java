package carcassonne.model.tile;

import static carcassonne.model.tile.TileDirection.*;
import static org.junit.Assert.*;

import carcassonne.model.Player;
import carcassonne.model.Feature.Feature;
import carcassonne.model.Feature.FeatureType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TileTest {
    private Tile tile;
    private Player player = new Player();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void initialize() {
        tile = Tile.getInstance();
    }

    @Test
    public void setXYgetXY() {
        tile = tile.setCoordinates(0, 0);
        assertEquals("X coordinate", 0, tile.getX());
        assertEquals("Y coordinate", 0, tile.getY());
    }

    @Test
    public void coordinateConstructorTest() {
        Tile tile = Tile.getInstance(0 ,0);
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
        tile = tile.setCoordinates(0, 0);
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
        assertEquals("No followers placed", false, tile.hasFollower());
    }

    @Test
    public void ifFollowerPlacedThenNoFollowersFalse() {
        tile = tile.placeFollower(player, Feature.createFeature(FeatureType.CITY));
        assertEquals("Follower is placed on tile", true, tile.hasFollower());
    }

    @Test
    public void ifHorizontalRoadTileThenRoadConnectsEastAndWest() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST, TileDirection.WEST);
        assertTrue("Horizontal road is connected west to east",
                tile.getDestinations(TileDirection.WEST).contains(TileDirection.EAST));
        assertTrue("Horizontal road is connected east to west",
                tile.getDestinations(TileDirection.EAST).contains(TileDirection.WEST));
    }

    @Test
    public void crossroadsIsNodConnecting() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);

        assertFalse("Crossroad does not connect EAST and WEST",
                tile.getDestinations(TileDirection.WEST).contains(TileDirection.EAST));
    }

    @Test
    public void horizontalRoadTileHasConnectingLands() {
        /*
         * The road
         */
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST, TileDirection.WEST);
        /*
         * The land
         */
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW, TileDirection.NNE, TileDirection.EEN);

        assertTrue("Land connects WWN and EEN",
                tile.getDestinations(TileDirection.WWN).contains(TileDirection.EEN));
    }

    @Test
    public void testCloister() {
        assertFalse("The tile doesn't have a cloister on it", tile.hasCloister());
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.CENTER);
        assertTrue("The tile has a cloister on it", tile.hasCloister());
    }

    @Test
    public void cloisterShouldNotConnectAnything() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.CENTER);
        assertEquals("Cloister connects itself to one TileDirection", 1, tile.getDestinations(TileDirection.CENTER).size());
        assertTrue("Cloister connects itself to itself",
                tile.getDestinations(TileDirection.CENTER).contains(TileDirection.CENTER));
    }

    @Test
    public void testTileCompletenessEmptyTileThenException() {
        assertFalse("Empty tile is not complete", tile.isComplete());
    }

    @Test
    public void completeTileNoCloisterThenFalse() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NNE, TileDirection.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWS, TileDirection.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EES, TileDirection.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void completeTileThenTrue() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NNE, TileDirection.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWS, TileDirection.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EES, TileDirection.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoThenFalse() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NNE, TileDirection.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWS, TileDirection.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EES, TileDirection.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoCloisterThenFalse() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NNE, TileDirection.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWS, TileDirection.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EES, TileDirection.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void propertyWithTileDirectionExistsWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionSameWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.EAST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.NORTH);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunctionWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunction2WhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST, TileDirection.EAST);
    }

    @Test
    public void getPropertiesReturnsProperties() {
        HashSet<Feature> expected = new HashSet<>();
        Feature feature = Feature.createFeature(FeatureType.CITY);
         /*
         * The road
         */
        tile.addFeature(feature, TileDirection.EAST, TileDirection.WEST);
        expected.add(feature);
        /*
         * The land
         */
        feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirection.WWN, TileDirection.NNW, TileDirection.NNE, TileDirection.EEN);
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
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile = tile.placeFollower(player, feature);
        assertTrue("Feature object that follower is placed on", feature.isSameType(tile.getOccupiedFeature()));
    }

    @Test
    /*
     * idea: returnFollowerToPlayer() returns follower to the player it belonged.
     * realization: players follower counter changes. Follower objects only exist when they are placed on tile
     */
    public void returnFollowerReturnsFollowerToThePlayer() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile = tile.placeFollower(player, feature);
        assertEquals("Number of followers a player has", 6, player.getNumberOfFollowers());
        assertTrue("Tile has a follower", tile.hasFollower());
        tile = tile.returnFollowerToPlayer();
        assertFalse("Tile should not have follower", tile.hasFollower());
        assertEquals("Number of followers a player has", 7, player.getNumberOfFollowers());
    }

    @Test
    public void returnFollowerWhenNoFollowerThenRuntimeException() {
        assertFalse("There are no followers", tile.hasFollower());
        exception.expect(RuntimeException.class);
        tile = tile.returnFollowerToPlayer();
    }

    @Test
    public void getOccupiedFeature() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile = tile.placeFollower(player, feature);
        HashSet<TileDirection> directionsSet = new HashSet<>();
        directionsSet.add(TileDirection.WEST);
        assertTrue("Returns tileDirection set of follower containing Feature", directionsSet.equals(tile.getOccupiedFeatureDirections()));
    }

    @Test
    public void placeFollowerOnTileDirectionNoFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.placeFollower(new Player(), TileDirection.WEST);
    }

    @Test
    public void placeFollowerOnDirectionCorrectTileDirection() {
        assertFalse("Tile has no followers", tile.hasFollower());
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirection.WEST);
        tile = tile.placeFollower(new Player(), TileDirection.WEST);
        assertTrue("Tile has a follower", tile.hasFollower());

        Set<TileDirection> expected = new HashSet<>();
        expected.add(TileDirection.WEST);
        assertTrue("TileDirection of Feature with follower", expected.equals(tile.getOccupiedFeatureDirections()));
    }

    @Test
    public void getUnoccupiedDirections() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EAST, TileDirection.NNW, TileDirection.SSE, TileDirection.EEN);
        Set<TileDirection> expected = new HashSet<>();
        expected.add(TileDirection.NORTH);
        expected.add(TileDirection.NNE);
        expected.add(TileDirection.EES);
        expected.add(TileDirection.SOUTH);
        expected.add(TileDirection.SSW);
        expected.add(TileDirection.WEST);
        expected.add(TileDirection.WWS);
        expected.add(TileDirection.WWN);
        TileDirection[] expectedArray = expected.toArray(new TileDirection[expected.size()]);
        assertEquals("Unoccupied directions excluding END and CENTER", new HashSet<>(Arrays.asList(expectedArray)),
                new HashSet<>(Arrays.asList(tile.getUnoccupiedDirections())));
    }


    @Test
    public void usingEndDirectionMoreThenOnce() {
        Feature feature1, feature2, feature3;
        feature1 = Feature.createFeature(FeatureType.CITY);
        feature2 = Feature.createFeature(FeatureType.CITY);
        feature3 = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature1, EAST, EEN, EES);
        tile.addFeature(feature2, WEST, WWN, WWS);
        tile.addFeature(feature3, tile.getUnoccupiedDirections());
        Set<Feature> expected = new HashSet<>(Arrays.asList(feature1, feature2, feature3));
        assertEquals("Tile features", expected, tile.getFeatures());
    }

    @Test
    public void rotationTestFeatureToTileDirections() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, EAST,EEN, NNE, NORTH);
        tile = tile.placeFollower(new Player(), EAST);
        tile = tile.turnClockwise(Rotation.DEG_90);
        Set<TileDirection> expected = new HashSet<>(Arrays.asList(SOUTH, SSE, EES, EAST));
        assertEquals("TileDirection of rotated tile", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotate270Degrees() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, EAST,EEN, NNE, NORTH);
        tile = tile.placeFollower(new Player(), EAST);
        tile = tile.turnClockwise(Rotation.DEG_270);
        Set<TileDirection> expected = new HashSet<>(Arrays.asList(NORTH, WWN, NNW, WEST));
        assertEquals("TileDirection of rotated tile", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotateENDandCENTER() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, CENTER);
        tile = tile.placeFollower(new Player(), CENTER);
        tile = tile.turnClockwise(Rotation.DEG_270);
        Set<TileDirection> expected = new HashSet<>(Collections.singletonList(CENTER));
        assertEquals("TileDirection of rotating CENTER and END", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotationTestFeatureGetDestinations() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, EAST,EEN, NNE, NORTH);
        tile = tile.turnClockwise(Rotation.DEG_90);
        Set<TileDirection> expected = new HashSet<>(Arrays.asList(SOUTH, SSE, EES, EAST));
        assertEquals("TileDirection of rotated tile", expected, tile.getDestinations(SOUTH));
    }

    @Test
    public void rotateRoadTurn() {
        Feature feature1, feature2, feature3;
        feature1 = Feature.createFeature(FeatureType.CITY);
        feature2 = Feature.createFeature(FeatureType.CITY);
        feature3 = Feature.createFeature(FeatureType.CITY);
        Tile roadTile = Tile.getInstance(1, 0);
        roadTile.addFeature(feature1, SOUTH, EAST);
        roadTile.addFeature(feature2, EES, SSE);
        roadTile.addFeature(feature3, roadTile.getUnoccupiedDirections());
        roadTile = roadTile.turnClockwise(Rotation.DEG_90);

        Tile expectedTile = Tile.getInstance(2,0);
        expectedTile.addFeature(feature1, SOUTH, WEST);
        expectedTile.addFeature(feature2, SSW, WWS);
        expectedTile.addFeature(feature3, expectedTile.getUnoccupiedDirections());

        assertTrue("Road is turned", expectedTile.featureEqual(roadTile));
    }

    @Test
    public void copyFeatures() {
        Tile tile = Tile.getInstance();
        tile = tile.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        assertTrue("New tile is Feature equal", tile.directionsEqual(TilePile.getReferenceTile(TileName.ROAD4)));
    }

    @Test
    public void smallCityContinuous() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        Tile tile2 = Tile.getInstance(TileName.CITY1);
        tile2 = tile2.turnClockwise(Rotation.DEG_180);
        assertEquals("Small city is continuous", true, tile1.isContinuous(tile2, TileDirection.SOUTH));
    }

    @Test
    public void smallCityNotContinuous() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        Tile tile2 = Tile.getInstance(TileName.CITY1);
        assertEquals("Small city is continuous", false, tile1.isContinuous(tile2, TileDirection.NORTH));
    }

    @Test
    public void tileSavesRotation() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        assertEquals("All tiles have 0 rotation by default", Rotation.DEG_0, tile1.getCurrentRotation());
    }

    @Test
    public void rotationChangesWhenTurned() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        tile1 = tile1.turnClockwise(Rotation.DEG_180);
        assertEquals("Rotation saved after turn", Rotation.DEG_180, tile1.getCurrentRotation());
        tile1 = tile1.turnClockwise(Rotation.DEG_270);
        assertEquals("Rotation saved after turn", Rotation.DEG_90, tile1.getCurrentRotation());
    }

}




















