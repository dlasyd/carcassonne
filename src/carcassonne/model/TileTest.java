package carcassonne.model;

import static carcassonne.model.TileDirections.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TileTest {
    public Tile tile;
    public Player player = new Player();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void initialize() {
        tile = Tile.getInstance();
    }

    @Test
    public void setXYgetXY() {
        tile.setCoordinates(0, 0);
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
        tile.placeFollower(player, Feature.createFeature(FeatureType.CITY));
        assertEquals("Follower is placed on tile", false, tile.isNoFollower());
    }

    @Test
    public void ifHorizontalRoadTileThenRoadConnectsEastAndWest() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST, TileDirections.WEST);
        assertTrue("Horizontal road is connected west to east",
                tile.getDestinations(TileDirections.WEST).contains(TileDirections.EAST));
        assertTrue("Horizontal road is connected east to west",
                tile.getDestinations(TileDirections.EAST).contains(TileDirections.WEST));
    }

    @Test
    public void crossroadsIsNodConnecting() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);

        assertFalse("Crossroad does not connect EAST and WEST",
                tile.getDestinations(TileDirections.WEST).contains(TileDirections.EAST));
    }

    @Test
    public void horizontalRoadTileHasConnectingLands() {
        /*
         * The road
         */
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST, TileDirections.WEST);
        /*
         * The land
         */
        tile.addFeature(Feature.createFeature(FeatureType.CITY),TileDirections.WWN, TileDirections.NNW, TileDirections.NNE, TileDirections.EEN);

        assertTrue("Land connects WWN and EEN",
                tile.getDestinations(TileDirections.WWN).contains(TileDirections.EEN));
    }

    @Test
    public void testCloister() {
        assertFalse("The tile doesn't have a cloister on it", tile.hasCloister());
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.CENTER);
        assertTrue("The tile has a cloister on it", tile.hasCloister());
    }

    @Test
    public void cloisterShouldNotConnectAnything() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.CENTER);
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
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void completeTileThenTrue() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EES, TileDirections.SSE);

        assertTrue("Tile is complete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoThenFalse() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void incompleteTileNoCloisterThenFalse() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EES, TileDirections.SSE);

        assertFalse("Tile is incomplete", tile.isComplete());
    }

    @Test
    public void propertyWithTileDirectionExistsWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionSameWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.EAST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.NORTH);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunctionWhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
    }

    @Test
    public void propertyWithTileDirectionExistsLongFunctionDifferentFunction2WhenAddingThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST, TileDirections.EAST);
    }

    @Test
    public void getPropertiesReturnsProperties() {
        HashSet<Feature> expected = new HashSet<>();
        Feature feature = Feature.createFeature(FeatureType.CITY);
         /*
         * The road
         */
        tile.addFeature(feature, TileDirections.EAST, TileDirections.WEST);
        expected.add(feature);
        /*
         * The land
         */
        feature = Feature.createFeature(FeatureType.CITY);
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
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.placeFollower(player, feature);
        assertEquals("Feature object that follower is placed on", feature, tile.getOccupiedFeature());
    }

    @Test
    /*
     * idea: returnFollower() returns follower to the player it belonged.
     * realization: players follower counter changes. Follower objects only exist when they are placed on tile
     */
    public void returnFollowerReturnsFollowerToThePlayer() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
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
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NORTH);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
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
        Feature feature = Feature.createFeature(FeatureType.CITY);
        tile.addFeature(feature, TileDirections.WEST);
        tile.placeFollower(new Player(), TileDirections.WEST);
        assertFalse("Tile has a follower", tile.isNoFollower());

        Set<TileDirections> expected = new HashSet<>();
        expected.add(TileDirections.WEST);
        assertTrue("TileDirection of feature with follower", expected.equals(tile.getOccupiedFeatureDirections()));
    }

    @Test
    public void getUnoccupiedDirections() {
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EAST, TileDirections.NNW, TileDirections.SSE, TileDirections.EEN);
        Set<TileDirections> expected = new HashSet<>();
        expected.add(TileDirections.NORTH);
        expected.add(TileDirections.NNE);
        expected.add(TileDirections.EES);
        expected.add(TileDirections.SOUTH);
        expected.add(TileDirections.SSW);
        expected.add(TileDirections.WEST);
        expected.add(TileDirections.WWS);
        expected.add(TileDirections.WWN);
        TileDirections[] expectedArray = expected.toArray(new TileDirections[expected.size()]);
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
        tile.placeFollower(new Player(), EAST);
        tile.turnRight(Rotation.DEG_90);
        Set<TileDirections> expected = new HashSet<>(Arrays.asList(SOUTH, SSE, EES, EAST));
        assertEquals("TileDirection of rotated tile", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotate270Degrees() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, EAST,EEN, NNE, NORTH);
        tile.placeFollower(new Player(), EAST);
        tile.turnRight(Rotation.DEG_270);
        Set<TileDirections> expected = new HashSet<>(Arrays.asList(NORTH, WWN, NNW, WEST));
        assertEquals("TileDirection of rotated tile", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotateENDandCENTER() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, CENTER);
        tile.placeFollower(new Player(), CENTER);
        tile.turnRight(Rotation.DEG_270);
        Set<TileDirections> expected = new HashSet<>(Collections.singletonList(CENTER));
        assertEquals("TileDirection of rotating CENTER and END", expected, tile.getOccupiedFeatureDirections());
    }

    @Test
    public void rotationTestFeatureGetDestinations() {
        Feature feature = Feature.createFeature(FeatureType.CITY);
        Tile tile = Tile.getInstance(0, 0);
        tile.addFeature(feature, EAST,EEN, NNE, NORTH);
        tile.turnRight(Rotation.DEG_90);
        Set<TileDirections> expected = new HashSet<>(Arrays.asList(SOUTH, SSE, EES, EAST));
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
        roadTile.turnRight(Rotation.DEG_90);

        Tile expectedTile = Tile.getInstance(2,0);
        expectedTile.addFeature(feature1, SOUTH, WEST);
        expectedTile.addFeature(feature2, SSW, WWS);
        expectedTile.addFeature(feature3, expectedTile.getUnoccupiedDirections());

        assertTrue("Road is turned", expectedTile.featureEqual(roadTile));
    }

    @Test
    public void copyFeatures() {
        Tile tile = Tile.getInstance();
        tile.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        assertTrue("New tile is feature equal", tile.directionsEqual(TilePile.getReferenceTile(TileName.ROAD4)));
    }

    @Test
    public void smallCityContinuous() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        Tile tile2 = Tile.getInstance(TileName.CITY1);
        tile2.turnRight(Rotation.DEG_180);
        assertEquals("Small city is continuous", true, tile1.isContinuous(tile2, TileDirections.SOUTH));
    }

    @Test
    public void smallCityNotContinuous() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        Tile tile2 = Tile.getInstance(TileName.CITY1);
        assertEquals("Small city is continuous", false, tile1.isContinuous(tile2, TileDirections.NORTH));
    }

    @Test
    public void tileSavesRotation() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        assertEquals("All tiles have 0 rotation by default", Rotation.DEG_0, tile1.getCurrentRotation());
    }

    @Test
    public void rotationChangesWhenTurned() {
        Tile tile1 = Tile.getInstance(0, 0, TileName.CITY1);
        tile1.turnRight(Rotation.DEG_180);
        assertEquals("Rotation saved after turn", Rotation.DEG_180, tile1.getCurrentRotation());
        tile1.turnRight(Rotation.DEG_270);
        assertEquals("Rotation saved after turn", Rotation.DEG_90, tile1.getCurrentRotation());
    }

}




















