package carcassonne.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static carcassonne.model.TileName.*;
import static org.junit.Assert.*;
import static carcassonne.model.TileDirections.*;

/**
 * This test class tests a lot of real game situations to make sure that RealEstate class behaves properly
 */
public class RealEstateTest {
    public Tile tile;
    public Tile tile_0_0, tile_1_0, tile_2_0, tile_1_1, tile_3_0, tile_4_0, tile_5_0, completeCrossroads_0_0, tile_6_0;
    public Tile tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2, tile_0_1, tile_0_2, tile_2_1, tile_1_2, tile_3_2;
    public Tile tile_2_2, tile_3_1;
    public RealEstate realEstate, realEstate2;
    public Table table;
    public RealEstateManager manager;
    public Player anton = new Player();
    public Player andrey = new Player();

    public void placeTile(int x, int y, TileName tileName, Rotation rotation) {
        Tile tile = Tile.getInstance(x, y);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        tile = tile.turnRight(rotation);
        table.placeTile(tile);

    }

    public void placeTile(int x, int y, TileName tileName, Rotation rotation, Player player, TileDirections tileDirection) {
        Tile tile = Tile.getInstance(x, y);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        tile = tile.turnRight(rotation);
        tile = tile.placeFollower(player, tileDirection);
        table.placeTile(tile);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        completeCrossroads_0_0 = Tile.getInstance(0, 0);
        table = new Table();
        RealEstate.setTable(table);
        completeCrossroads_0_0 = completeCrossroads_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        completeCrossroads_0_0 = completeCrossroads_0_0.placeFollower(new Player(), TileDirections.WEST);
        realEstate = RealEstate.getInstance(completeCrossroads_0_0);
        tile_0_0 = Tile.getInstance(0 ,0);
        tile_1_0 = Tile.getInstance(1 ,0);
        tile_2_0 = Tile.getInstance(2 ,0);
        tile_3_0 = Tile.getInstance(3, 0);
        tile_4_0 = Tile.getInstance(4, 0);
        tile_5_0 = Tile.getInstance(5, 0);
        tile_6_0 = Tile.getInstance(6, 0);
        tile_1_1 = Tile.getInstance(1 ,1);
        tile_2_1 = Tile.getInstance(2, 1);
        tile_1_m1 = Tile.getInstance(1, -1);
        tile_1_m2 = Tile.getInstance(1, -2);
        tile_2_m1 = Tile.getInstance(2, -1);
        tile_2_m2 = Tile.getInstance(2, -2);
        tile_3_m2 = Tile.getInstance(3, -2);

        tile_1_2 = Tile.getInstance(1, 2);
        tile_3_2 = Tile.getInstance(3, 2);
        tile_2_2 = Tile.getInstance(2, 2);
        tile_3_1 = Tile.getInstance(3, 1);

        tile_0_1 = Tile.getInstance(0, 1);
        tile_0_2 = Tile.getInstance(0, 2);

        table = new Table();
        manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance();
        tile = tile.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addTile(tile);
    }

    @Test
    public void addTileIncompleteThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance(1 ,1);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.SOUTH);

        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirections.EES, TileDirections.SSE);
        realEstate.addTile(tile);
    }

    @Test
    public void addTileSameCoordinatesThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = Tile.getInstance(0 ,0);
        Tile tile2 = Tile.getInstance(0 ,0);
        tile1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile2.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

    @Test
    public void addTileDisjointThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addTile(tile_0_0);
        realEstate.addTile(tile_1_1);
    }

    @Test
    public void addNullTileThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile nullTile = Tile.getNullInstance();
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_0_0);
        realEstate.addTile(nullTile);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        RealEstate.getInstance(tile_0_0);
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);
        table.placeTile(tile_1_0);
        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);
        table.placeTile(tile_2_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_1_0);
        expectedSet.add(tile_2_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnUnconnectedRealEstateFeature() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);
        table.placeTile(tile_1_0);
        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirections.EAST);

        table.placeTile(tile_2_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_1_0);
        expectedSet.add(tile_2_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnConnectedRealEstateFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_0_0 = tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0 = tile_0_0.placeFollower(new Player(), TileDirections.WEST);
        RealEstate realEstate = RealEstate.getInstance(tile_0_0);
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);

        realEstate.addTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void followerOnOccupiedRealEstateThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);

        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180, new Player(), EAST);
        placeTile(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        placeTile(3, 0, TileName.ROAD2SW, Rotation.DEG_0, new Player(), WEST);

    }


    @Test
    public void addThirdTileToPropertyFollowerOnFirst() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);

        placeTile(1, 0, TileName.ROAD4, Rotation.DEG_0, anton, EAST);
        placeTile(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        placeTile(3, 0, TileName.ROAD2NS, Rotation.DEG_90);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, Util.any(manager.getAssets(anton)).getTileSet());
    }

    @Test
    public void addThirdTileToPropertyFollowerOnSecond() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_2_0);

        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);

        table.placeTile(tile_1_0);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    /*
     * tile_1_0 and tile_3_0 are on the table before we put tile_2_0 and create a real estate
     */
    @Test
    public void createPropertyFromMiddleTile() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4));

        table.placeTile(tile_1_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_2_0);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirections.EAST);

        RealEstate realEstate = RealEstate.getInstance(tile_2_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertTrue("Three tiles are added", expected.equals(realEstate.getTileSet()));

    }

    /*
     * tile_1_0, tile_2_0, tile_4_0, tile_5_0 are on the table before we put tile_3_0 and create real estate
     * Real estate in test is a 5 - tile long horizontal road
     */
    @Test
    public void createPropertyFromManyPreviouslyPlacedTiles() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_5_0 = tile_5_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0 = tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0 = tile_4_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_4_0);
        table.placeTile(tile_5_0);

        table.placeTile(tile_3_0);
        tile_3_0 = tile_3_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0, tile_5_0));
        assertEquals("Five tiles are added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * Same as above but three tiles to the left when creating real estate (tile_0_0 does not count)
     */
    @Test
    public void createPropertyFromPreviousThreeToTheLeft() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_6_0 = tile_6_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0 = tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0 = tile_4_0.turnRight(Rotation.DEG_90);
        tile_5_0 = tile_5_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_5_0 = tile_5_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_5_0);
        table.placeTile(tile_6_0);

        table.placeTile(tile_4_0);

        tile_4_0 = tile_4_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_4_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0, tile_5_0, tile_6_0));
        assertEquals("Five tiles are added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * Finding adjacent tiles when they are placed vertically
     */
    @Test
    public void verticalTest() {
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_2 = tile_0_2.copyFeatures(TilePile.getReferenceTile(ROAD4));
        table.placeTile(tile_0_2);
        table.placeTile(tile_0_1);
        tile_0_1 = tile_0_1.placeFollower(new Player(), SOUTH);
        RealEstate realEstate = RealEstate.getInstance(tile_0_1);
        Set<Tile> expected = new HashSet<>();
        expected.add(tile_0_1);
        expected.add(tile_0_2);
        assertEquals("Real estate tiles", expected, realEstate.getTileSet());
    }

    /*
     * Applies to next two tests
     * This test of a complex real estate, both x and y change, one of the tile has more than 1 direction,
     * several TileDirections that characterise feature belong to one side of a tile
     */
    @Test
    public void createPropertyFromLessComplexPreviousTilePlacement() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1));
        tile_1_m1 = tile_1_m1.copyFeatures(TilePile.getReferenceTile(CITY3));
        tile_1_m1 = tile_1_m1.turnRight(Rotation.DEG_90);
        tile_1_m2 = tile_1_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));
        tile_1_m2 = tile_1_m2.turnRight(Rotation.DEG_90);
        tile_2_m1 = tile_2_m1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2 = tile_2_m2.copyFeatures(TilePile.getReferenceTile(CITY2NW));

        table.placeTile(tile_1_m1);
        table.placeTile(tile_1_m2);
        table.placeTile(tile_1_0);
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.NORTH);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);
        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_m1, tile_1_m2));
        assertEquals("Six tiles are added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void createPropertyFromComplexPreviousTilePlacement() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1));
        tile_1_m1 = tile_1_m1.copyFeatures(TilePile.getReferenceTile(CITY3));
        tile_1_m1 = tile_1_m1.turnRight(Rotation.DEG_90);
        tile_1_m2 = tile_1_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));
        tile_1_m2 = tile_1_m2.turnRight(Rotation.DEG_90);
        tile_2_m1 = tile_2_m1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2 = tile_2_m2.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2 = tile_2_m2.turnRight(Rotation.DEG_180);
        tile_3_m2 = tile_3_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));

        table.placeTile(tile_1_m1);
        table.placeTile(tile_1_m2);
        table.placeTile(tile_2_m1);
        table.placeTile(tile_2_m2);
        table.placeTile(tile_3_m2);
        table.placeTile(tile_1_0);
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.NORTH);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);
        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2));
        assertEquals("Six tiles are added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void loopedRoadProperty() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_1 = tile_1_1.turnRight(Rotation.DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_2_1 = tile_2_1.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_2_1 = tile_2_1.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        table.placeTile(tile_1_1);
        table.placeTile(tile_2_0);
        table.placeTile(tile_2_1);

        tile_1_0 = tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_1, tile_2_0, tile_2_1));
        assertEquals("Road loop added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * When tile is placed on the table, it should be added to all real estate object that are appropriate.
     * This is relevant for next few tests
     */
    @Test
    public void addingTileToRealEstate() {
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_2_0);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addToTableDoNotAddToRealEstate() {
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_m1 = tile_2_m1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_m1 = tile_2_m1.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_2_0);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_2_m1);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateNotNearCreationTile() {
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_1_0);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateFarFromCreationTile() {

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0 = tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0 = tile_4_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_3_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_1_0);
        realEstate = RealEstate.getInstance(tile_1_0);
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_4_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateLoopedRoadProperty() {
        /*
         * 3x3 road square cycle
         */
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_2 = tile_1_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_2 = tile_1_2.turnRight(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2 = tile_3_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2 = tile_3_2.turnRight(Rotation.DEG_90);

        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_1 = tile_3_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));

        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_2 = tile_2_2.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_2 = tile_2_2.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        tile_1_0 = tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);

        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);

        table.placeTile(tile_2_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_3_1);
        table.placeTile(tile_3_2);
        table.placeTile(tile_2_2);
        table.placeTile(tile_1_2);
        table.placeTile(tile_1_1);



        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_1, tile_1_2, tile_2_0, tile_2_2,
                tile_3_0, tile_3_1, tile_3_2));
        assertEquals("Road loop added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void placeTileThenAddAdjacentTiles() {
        /*
         * 3x3 road square cycle
         */
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_2 = tile_1_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_2 = tile_1_2.turnRight(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2 = tile_3_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2 = tile_3_2.turnRight(Rotation.DEG_90);

        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_1 = tile_3_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));

        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_2 = tile_2_2.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_2 = tile_2_2.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        tile_1_0 = tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_1_0);

        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);
        table.placeTile(tile_3_0);
        table.placeTile(tile_3_1);
        table.placeTile(tile_3_2);
        table.placeTile(tile_2_2);
        table.placeTile(tile_1_2);


        table.placeTile(tile_2_0);
        table.placeTile(tile_1_1);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_1, tile_1_2, tile_2_0, tile_2_2,
                tile_3_0, tile_3_1, tile_3_2));
        assertEquals("Road loop added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void sameWhenUnion() {
        Player andrey = new Player();
        Player anton = new Player();

        placeTile(1, 0, TileName.ROAD2NS, Rotation.DEG_90, andrey, EAST);
        placeTile(3, 0, TileName.ROAD2NS, Rotation.DEG_90, anton,  EAST);
        placeTile(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        assertEquals("Players should have same real estate object", manager.getAssets(anton), manager.getAssets(andrey));
    }

    @Test
    public void checkTileDirectionsOfRealEstate() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);

        Map<Tile, Set<TileDirections>> expected = new HashMap<>();
        expected.put(tile_1_0, new HashSet<>(Arrays.asList(NORTH, EAST)));
        expected.put(tile_3_0, new HashSet<>(Arrays.asList(WEST, SOUTH)));
        expected.put(tile_2_0, new HashSet<>(Arrays.asList(WEST, EAST)));

        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180, new Player(), TileDirections.EAST);
        placeTile(3, 0, TileName.ROAD2SW, Rotation.DEG_0);
        placeTile(2, 0, TileName.ROAD2NS, Rotation.DEG_90);

        Set<RealEstate.ImmutableRealEstate> keys = manager.getRealEstateImmutableSet();
        Map<Tile, Set<TileDirections>> realResult = new HashMap<>();
        for (RealEstate.ImmutableRealEstate key: keys) {
            realResult = key.getRealEstate().getTilesAndFeatureTileDirections();
            break;
        }
        assertEquals("Tiles and tile directions", expected, realResult);
    }

    @Test
    public void completeRealEstateAddedProperly() {
        table.setRealEstateManager(manager);
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));

        placeTile(1, 0, TileName.ROAD4, Rotation.DEG_0);
        placeTile(2, 0, TileName.ROAD4, Rotation.DEG_0, new Player(), WEST);

        Map<Tile, Set<TileDirections>> expected = new HashMap();
        expected.put(tile_2_0, new HashSet<>(Collections.singletonList(WEST)));
        expected.put(tile_1_0, new HashSet<>(Collections.singletonList(EAST)));

        Map<Tile, Set<TileDirections>> tileToTileDirections = new HashMap();

        /*
         * TODO refactor this ugly thing
         */
        for (Player player: manager.getPlayerToFinishedRealEstate().keySet()) {
            for (RealEstate.ImmutableRealEstate realEstate: manager.getPlayerToFinishedRealEstate().get(player)) {
                tileToTileDirections = realEstate.getRealEstate().getTilesAndFeatureTileDirections();
            }
        }
        Tile tile1 = null, tile2 = null;
        for (Tile tile: expected.keySet()) {
            if (tile.getCoordinates().equals(new Coordinates(2,0)))
                tile1 = tile;
        }
        for (Tile tile: tileToTileDirections.keySet()) {
            if (tile.getCoordinates().equals(new Coordinates(2,0)))
                tile2 = tile;
        }
        boolean h = tile1.equals(tile2);
        assertEquals("Equal tiles are equal", tile1, tile2);
        assertEquals ("Tile consist of correct tileDirections", expected, tileToTileDirections);
    }

    @Test
    public void roadIsFinishedWhenLoop() {

    }

    @Test
    public void roadIsFinishedWhenTwoEnds() {
        Player anton = new Player();
        placeTile(1, 0, TileName.ROAD4, Rotation.DEG_180);
        placeTile(2, 0, TileName.ROAD4, Rotation.DEG_0, anton, WEST);

        assertEquals ("Player has no unfinished real estate", false, manager.playerHasAssets(anton));
    }

    @Test
    public void tilesAddedToCloister() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_1_1 = tile_1_1.placeFollower(new Player(), TileDirections.CENTER);
        tile_1_2 = tile_1_2.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_2_2 = tile_2_2.copyFeatures(TilePile.getReferenceTile(CLOISTER));

        table.placeTile(tile_1_0);
        RealEstate realEstate = RealEstate.getInstance(tile_1_1);
        table.placeTile(tile_1_1);
        RealEstateManager manager = new RealEstateManager(table);
        manager.addAsset(new Player(), realEstate);
        table.setRealEstateManager(manager);
        table.placeTile(tile_0_1);
        table.placeTile(tile_1_2);
        table.placeTile(tile_2_2);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(table.getFirstTile());
        expectedSet.add(tile_1_0);
        expectedSet.add(tile_0_1);
        expectedSet.add(tile_1_1);
        expectedSet.add(tile_1_2);
        expectedSet.add(tile_2_2);

        assertEquals("Cloister has correct tiles", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void landNoFinishedCity() {
        placeTile(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirections.SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 0, anton.getCurrentPoints());
    }

    @Test
    public void landFinishedAndUnfinishedCity() {
        placeTile(1, 1, TileName.CITY1, Rotation.DEG_180);
        placeTile(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirections.SOUTH);
        placeTile(1, 3, TileName.CITY1, Rotation.DEG_180);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 3, anton.getCurrentPoints());
    }

    @Test
    public void landFinishedCity() {
        placeTile(1, 0, TileName.CLOISTER, Rotation.DEG_0);
        placeTile(1, 1, TileName.CITY1, Rotation.DEG_180);
        placeTile(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirections.SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 3, anton.getCurrentPoints());
    }

    @Test
    public void land2FinishedCities() {
        placeTile(1, 1, TileName.CITY1, Rotation.DEG_180);
        placeTile(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirections.SOUTH);
        placeTile(1, 3, TileName.CITY1, Rotation.DEG_180);
        placeTile(1, 4, TileName.CITY1, Rotation.DEG_0);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 6, anton.getCurrentPoints());
    }

    @Test
    public void landWith2FinishedCitiesRussianG() {
        placeTile(1, 2, TileName.CITY11NE, Rotation.DEG_0, anton, SOUTH);
        placeTile(1, 1, TileName.CITY1, Rotation.DEG_180);
        placeTile(2, 2, TileName.CITY1, Rotation.DEG_270);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Two castles are finished", 6, anton.getCurrentPoints());
    }

    @Test
    public void castleWithOneShield() {
        placeTile(1, 1, TileName.CITY1, Rotation.DEG_180, anton, SOUTH);
        placeTile(1, 2, TileName.CITY2NWS, Rotation.DEG_0);
        placeTile(0, 2, TileName.CITY1, Rotation.DEG_90);
        assertEquals("Three tile castle with one shield is 8 points", 8, anton.getCurrentPoints());
    }

    @Test
    public void twoCloistersNextToEachOtherArePossible() {
        placeTile(1, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        placeTile(2, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        assertEquals("Anton has 5 followers", 5, anton.getNumberOfFollowers());
    }

    @Test
    public void bigUnfinishedCastleGivesALotOfPoints() {
        placeTile(0,  -1, TileName.CITY11WE,  Rotation.DEG_90);
        placeTile(0,  -2, TileName.CITY2WES,  Rotation.DEG_90);
        placeTile(0,  -3, TileName.CITY4,     Rotation.DEG_0);
        placeTile(-1, -3, TileName.CITY2NW,   Rotation.DEG_90);
        placeTile(1,  -3, TileName.CITY2NWR,  Rotation.DEG_0);
        placeTile(0,  -4, TileName.CITY3R,    Rotation.DEG_90);
        placeTile(1,  -4, TileName.CITY2NWSR, Rotation.DEG_270);
        placeTile(-1, -4, TileName.CITY1RSWE, Rotation.DEG_180);
        placeTile(0,  -5, TileName.CITY2WE,   Rotation.DEG_90, anton, SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Anton has 12 points for unfinished castle", 12, anton.getCurrentPoints());
    }
}