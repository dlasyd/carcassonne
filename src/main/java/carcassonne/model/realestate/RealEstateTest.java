package carcassonne.model.realestate;

import carcassonne.model.*;
import carcassonne.model.Feature.Feature;
import carcassonne.model.Feature.FeatureType;
import carcassonne.model.tile.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.stream.Collectors;

import static carcassonne.model.tile.TileName.*;
import static org.junit.Assert.*;
import static carcassonne.model.tile.TileDirection.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test class tests a lot of real game situations to make sure that RealEstate class behaves properly
 */
public class RealEstateTest {
    public Tile tile;
    public Tile tile_0_0, tile_1_0, tile_2_0, tile_1_1, tile_3_0, tile_4_0, tile_5_0, completeCrossroads_0_0, tile_6_0;
    public Tile tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2, tile_0_1, tile_0_2, tile_2_1, tile_1_2, tile_3_2;
    public Tile tile_2_2, tile_3_1;
    public RealEstate realEstate, realEstate2;
    public RealEstateManager manager;
    public Player anton = new Player();
    public Player andrey = new Player();
    public Table table;
    public TilesOnTable mTable;
    public SimpleTable simpleTable;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private Table mockTable;

    public Tile createAndAddToTable(int x, int y, TileName tileName, Rotation rotation) {
        Tile tile = Tile.getInstance(x, y, tileName);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        tile = tile.turnRight(rotation);
        simpleTable.placeTile(tile);
        return tile;
    }

    public Tile createAndAddToTable(int x, int y, TileName tileName, Rotation rotation, Player player, TileDirection tileDirection) {
        Tile tile = Tile.getInstance(x, y, tileName);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        tile = tile.turnRight(rotation);
        tile = tile.placeFollower(player, tileDirection);
        simpleTable.placeTile(tile);
        return tile;
    }

    @Before
    public void setUp() {
        table = new Table();
        completeCrossroads_0_0 = Tile.getInstance(0, 0);
        simpleTable = new SimpleTable();
        mTable = simpleTable;

        completeCrossroads_0_0 = completeCrossroads_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        completeCrossroads_0_0 = completeCrossroads_0_0.placeFollower(new Player(), TileDirection.WEST);
        realEstate = RealEstate.getInstance(completeCrossroads_0_0, table);
        tile_0_0 = Tile.getInstance(0, 0);
        tile_1_0 = Tile.getInstance(1, 0);
        tile_2_0 = Tile.getInstance(2, 0);
        tile_3_0 = Tile.getInstance(3, 0);
        tile_4_0 = Tile.getInstance(4, 0);
        tile_5_0 = Tile.getInstance(5, 0);
        tile_6_0 = Tile.getInstance(6, 0);
        tile_1_1 = Tile.getInstance(1, 1);
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

        manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        mockTable = mock(Table.class);
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance();
        tile = tile.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addFirstTile(tile);
    }

    @Test
    public void addTileIncompleteThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance(1, 1);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WEST);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.SOUTH);

        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.CENTER);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWN, TileDirection.NNW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.NNE, TileDirection.EEN);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.WWS, TileDirection.SSW);
        tile.addFeature(Feature.createFeature(FeatureType.CITY), TileDirection.EES, TileDirection.SSE);
        realEstate.addFirstTile(tile);
    }

    @Test
    public void addTileSameCoordinatesThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = Tile.getInstance(0, 0);
        Tile tile2 = Tile.getInstance(0, 0);
        tile1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile2.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addFirstTile(tile1);
        realEstate.addFirstTile(tile2);
    }

    @Test
    public void addTileDisjointThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addFirstTile(tile_0_0);
        realEstate.addFirstTile(tile_1_1);
    }

    @Test
    public void addNullTileThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile nullTile = Tile.getNullInstance();
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirection.EAST);
        RealEstate realEstate = RealEstate.getInstance(tile_0_0, table);
        realEstate.addFirstTile(nullTile);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        RealEstate.getInstance(tile_0_0, table);
    }

    @Test
    public void createRealEstate() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirection.EAST);
        when(mockTable.getNeighbouringTile(any(Integer.class), any(Integer.class), any(TileDirection.class)))
                .thenReturn(Tile.getNullInstance());
        RealEstate realEstate = RealEstate.getInstance(tile_1_0, mockTable);
        assertEquals(new HashSet(Arrays.asList(tile_1_0)), realEstate.getTileSet());
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4));

        when(mockTable.getNeighbouringTile(any(Integer.class), any(Integer.class), any(TileDirection.class)))
                .thenReturn(Tile.getNullInstance());
        when(mockTable.getNeighbouringTile(1, 0, TileDirection.EAST)).thenReturn(tile_2_0);

        RealEstate realEstate = RealEstate.getInstance(tile_1_0, mockTable);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_1_0);
        expectedSet.add(tile_2_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnUnconnectedRealEstateFeature() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4))
                .placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4))
                .placeFollower(new Player(), TileDirection.EAST);

        when(mockTable.getNeighbouringTile(any(Integer.class), any(Integer.class), any(TileDirection.class)))
                .thenReturn(Tile.getNullInstance());
        when(mockTable.getNeighbouringTile(2, 0, TileDirection.EAST)).thenReturn(tile_2_0);

        realEstate = RealEstate.getInstance(tile_1_0, mockTable);

        when(mockTable.getTile(any(Integer.class), any(Integer.class)))
                .thenReturn(Tile.getNullInstance());
        when(mockTable.getTile(1, 0)).thenReturn(tile_1_0);
        realEstate.update(tile_2_0);


        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_1_0);
        expectedSet.add(tile_2_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnConnectedRealEstateFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_0_0 = tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0 = tile_0_0.placeFollower(new Player(), TileDirection.WEST);
        RealEstate realEstate = RealEstate.getInstance(tile_0_0, null);
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirection.EAST);

        realEstate.addFirstTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    // TODO move to RealEstateManagerTest
    @Test
    public void followerOnOccupiedRealEstateThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);

        createAndAddToTable(1, 0, TileName.ROAD2SW, Rotation.DEG_180, new Player(), EAST);
        createAndAddToTable(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        createAndAddToTable(3, 0, TileName.ROAD2SW, Rotation.DEG_0, new Player(), WEST);

    }


    @Test
    public void addThirdTileToPropertyFollowerOnFirst() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS).turnRight(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS).turnRight(Rotation.DEG_90);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_3_0);

        assertEquals("Three tiles are added", new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void addThirdTileToPropertyFollowerOnSecond() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_3_0);

        assertEquals("Three tiles are added", new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    /*
     * tile_1_0 and tile_3_0 are on the table before we put tile_2_0 and create a real estate
     */
    @Test
    public void createPropertyFromMiddleTile() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_3_0 = Tile.getInstance(3, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2SW)
                .turnRight(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_3_0, tile_2_0);
        assertEquals("Three tiles are added", new HashSet(expected), placeAndUpdate(expected).getTileSet());

    }

    /*
     * tile_1_0, tile_2_0, tile_4_0, tile_5_0 are on the table before we put tile_3_0 and create real estate
     * Real estate in test is a 5 - tile long horizontal road
     */
    @Test
    public void createPropertyFromManyPreviouslyPlacedTiles() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_5_0 = Tile.getInstance(5, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);
        tile_4_0 = Tile.getInstance(4, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_4_0, tile_5_0, tile_3_0);
        assertEquals("Five tiles are added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    /*
     * Same as above but three tiles to the left when creating real estate (tile_0_0 does not count)
     */
    @Test
    public void createPropertyFromPreviousThreeToTheLeft() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);
        tile_5_0 = Tile.getInstance(5, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90);
        tile_6_0 = Tile.getInstance(6, 0, ROAD4);
        tile_4_0 = Tile.getInstance(4, 0, ROAD2NS)
                .turnRight(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_5_0, tile_6_0, tile_4_0);
        assertEquals("Five tiles are added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    /*
     * Finding adjacent tiles when they are placed vertically
     */
    @Test
    public void verticalTest() {
        tile_0_1 = Tile.getInstance(0, 1, ROAD4)
                .placeFollower(new Player(), SOUTH);
        tile_0_2 = Tile.getInstance(0, 2, ROAD4);
        List<Tile> expected = Arrays.asList(tile_0_1, tile_0_2);
        assertEquals(new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void createPropertyFromLessComplexPreviousTilePlacement() {
        tile_1_m1 = Tile.getInstance(1, -1, CITY3)
                .turnRight(Rotation.DEG_90);
        tile_1_m2 = Tile.getInstance(1, -2, CITY2WE)
                .turnRight(Rotation.DEG_90);
        tile_1_0 = Tile.getInstance(1, 0, CITY1).placeFollower(new Player(), TileDirection.NORTH);

        tile_2_m1 = Tile.getInstance(2, -1, CITY2NW);
        tile_2_m2 = Tile.getInstance(2, -2, CITY2NW);
        List<Tile> expected = Arrays.asList(tile_1_m1, tile_1_m2, tile_1_0);
        assertEquals(new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void createPropertyFromComplexPreviousTilePlacement() {
        tile_1_m1 = Tile.getInstance(1, -1, CITY3)
                .turnRight(Rotation.DEG_90);
        tile_1_m2 = Tile.getInstance(1, -2, CITY2WE)
                .turnRight(Rotation.DEG_90);
        tile_2_m1 = Tile.getInstance(2, -1, CITY2NW);
        tile_2_m2 = Tile.getInstance(2, -2, CITY2NW)
                .turnRight(Rotation.DEG_180);
        tile_3_m2 = Tile.getInstance(3, -2, CITY2WE);
        tile_1_0 = Tile.getInstance(1, 0, CITY1)
                .placeFollower(new Player(), TileDirection.NORTH);

        List<Tile> expected = Arrays.asList(tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2, tile_1_0);
        assertEquals("Six tiles are added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void loopedRoadProperty() {
        tile_1_1 = Tile.getInstance(1, 1, ROAD2SW)
                .turnRight(Rotation.DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2SW);
        tile_2_1 = Tile.getInstance(2, 1, ROAD2SW)
                .turnRight(Rotation.DEG_90);
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnRight(Rotation.DEG_270)
                .placeFollower(new Player(), EAST);


        List<Tile> expected = Arrays.asList(tile_1_1, tile_2_0, tile_2_1, tile_1_0);
        assertEquals("Road loop added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
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
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirection.EAST);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        realEstate = RealEstate.getInstance(tile_2_0, null);
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
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirection.EAST);
        tile_2_m1 = tile_2_m1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_m1 = tile_2_m1.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_2_0, null);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_2_m1);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        assertEquals("Two tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateNotNearCreationTile() {
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_1_0, null);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateFarFromCreationTile() {

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0 = tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0 = tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0 = tile_4_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_3_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_1_0);
        realEstate = RealEstate.getInstance(tile_1_0, null);
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
        RealEstate realEstate = RealEstate.getInstance(tile_1_0, null);

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
        RealEstate realEstate = RealEstate.getInstance(tile_1_0, null);

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

        createAndAddToTable(1, 0, TileName.ROAD2NS, Rotation.DEG_90, andrey, EAST);
        createAndAddToTable(3, 0, TileName.ROAD2NS, Rotation.DEG_90, anton, EAST);
        createAndAddToTable(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        assertEquals("Players should have same real estate object", manager.getAssets(anton), manager.getAssets(andrey));
    }

    @Test
    public void checkTileDirectionsOfRealEstate() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW);
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2SW);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS);
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);

        Map<Tile, Set<TileDirection>> expected = new HashMap<>();
        expected.put(tile_1_0, new HashSet<>(Arrays.asList(NORTH, EAST)));
        expected.put(tile_3_0, new HashSet<>(Arrays.asList(WEST, SOUTH)));
        expected.put(tile_2_0, new HashSet<>(Arrays.asList(WEST, EAST)));

        createAndAddToTable(1, 0, TileName.ROAD2SW, Rotation.DEG_180, new Player(), TileDirection.EAST);
        createAndAddToTable(3, 0, TileName.ROAD2SW, Rotation.DEG_0);
        createAndAddToTable(2, 0, TileName.ROAD2NS, Rotation.DEG_90);

        Set<RealEstate.ImmutableRealEstate> keys = manager.getRealEstateImmutableSet();
        Map<Tile, Set<TileDirection>> realResult = new HashMap<>();
        realResult = keys.iterator().next().getRealEstate().getTilesAndFeatureTileDirections();
        assertEquals("Tiles and tile directions", expected, realResult);
    }

    @Test
    public void completeRealEstateAddedProperly() {
        table.setRealEstateManager(manager);

        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD4);

        createAndAddToTable(1, 0, TileName.ROAD4, Rotation.DEG_0);
        createAndAddToTable(2, 0, TileName.ROAD4, Rotation.DEG_0, new Player(), WEST);

        Map<Tile, Set<TileDirection>> expected = new HashMap<>();
        expected.put(tile_2_0, new HashSet<>(Collections.singletonList(WEST)));
        expected.put(tile_1_0, new HashSet<>(Collections.singletonList(EAST)));

        Map<Tile, Set<TileDirection>> tileToTileDirections = new HashMap<>();

        /*
         * TODO refactor this ugly thing
         */
        for (Player player : manager.getPlayerToFinishedRealEstate().keySet()) {
            for (RealEstate.ImmutableRealEstate realEstate : manager.getPlayerToFinishedRealEstate().get(player)) {
                tileToTileDirections = realEstate.getRealEstate().getTilesAndFeatureTileDirections();
            }
        }
        Tile tile1 = null, tile2 = null;
        for (Tile tile : expected.keySet()) {
            if (tile.getCoordinates().equals(new Coordinates(2, 0)))
                tile1 = tile;
        }
        for (Tile tile : tileToTileDirections.keySet()) {
            if (tile.getCoordinates().equals(new Coordinates(2, 0)))
                tile2 = tile;
        }
        boolean h = tile1.equals(tile2);
        assertEquals("Equal tiles are equal", tile1, tile2);
        assertEquals("Tile consist of correct tileDirections", expected, tileToTileDirections);
    }

    @Test
    public void roadIsFinishedWhenTwoEnds() {
        Player anton = new Player();
        createAndAddToTable(1, 0, TileName.ROAD4, Rotation.DEG_180);
        createAndAddToTable(2, 0, TileName.ROAD4, Rotation.DEG_0, anton, WEST);

        assertEquals("Player has no unfinished real estate", false, manager.playerHasAssets(anton));
    }

    @Test
    public void tilesAddedToCloister() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_1_1 = tile_1_1.placeFollower(new Player(), TileDirection.CENTER);
        tile_1_2 = tile_1_2.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CLOISTER));
        tile_2_2 = tile_2_2.copyFeatures(TilePile.getReferenceTile(CLOISTER));

        table.placeTile(tile_1_0);
        RealEstate realEstate = RealEstate.getInstance(tile_1_1, null);
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
        createAndAddToTable(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirection.SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 0, anton.getCurrentPoints());
    }

    @Test
    public void landFinishedAndUnfinishedCity() {
        createAndAddToTable(1, 1, TileName.CITY1, Rotation.DEG_180);
        createAndAddToTable(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirection.SOUTH);
        createAndAddToTable(1, 3, TileName.CITY1, Rotation.DEG_180);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 3, anton.getCurrentPoints());
    }

    @Test
    public void landFinishedCity() {
        createAndAddToTable(1, 0, TileName.CLOISTER, Rotation.DEG_0);
        createAndAddToTable(1, 1, TileName.CITY1, Rotation.DEG_180);
        createAndAddToTable(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirection.SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 3, anton.getCurrentPoints());
    }

    @Test
    public void land2FinishedCities() {
        createAndAddToTable(1, 1, TileName.CITY1, Rotation.DEG_180);
        createAndAddToTable(1, 2, TileName.CITY1, Rotation.DEG_0, anton, TileDirection.SOUTH);
        createAndAddToTable(1, 3, TileName.CITY1, Rotation.DEG_180);
        createAndAddToTable(1, 4, TileName.CITY1, Rotation.DEG_0);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Player has no points", 6, anton.getCurrentPoints());
    }

    @Test
    public void landWith2FinishedCitiesRussianG() {
        createAndAddToTable(1, 2, TileName.CITY11NE, Rotation.DEG_0, anton, SOUTH);
        createAndAddToTable(1, 1, TileName.CITY1, Rotation.DEG_180);
        createAndAddToTable(2, 2, TileName.CITY1, Rotation.DEG_270);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Two castles are finished", 6, anton.getCurrentPoints());
    }

    @Test
    public void castleWithOneShield() {
        createAndAddToTable(1, 1, TileName.CITY1, Rotation.DEG_180, anton, SOUTH);
        createAndAddToTable(1, 2, TileName.CITY2NWS, Rotation.DEG_0);
        createAndAddToTable(0, 2, TileName.CITY1, Rotation.DEG_90);
        assertEquals("Three tile castle with one shield is 8 points", 8, anton.getCurrentPoints());
    }

    @Test
    public void twoCloistersNextToEachOtherArePossible() {
        createAndAddToTable(1, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        createAndAddToTable(2, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        assertEquals("Anton has 5 followers", 5, anton.getNumberOfFollowers());
    }

    @Test
    public void bigUnfinishedCastleGivesALotOfPoints() {
        createAndAddToTable(0, -1, TileName.CITY11WE, Rotation.DEG_90);
        createAndAddToTable(0, -2, TileName.CITY2WES, Rotation.DEG_90);
        createAndAddToTable(0, -3, TileName.CITY4, Rotation.DEG_0);
        createAndAddToTable(-1, -3, TileName.CITY2NW, Rotation.DEG_90);
        createAndAddToTable(1, -3, TileName.CITY2NWR, Rotation.DEG_0);
        createAndAddToTable(0, -4, TileName.CITY3R, Rotation.DEG_90);
        createAndAddToTable(1, -4, TileName.CITY2NWSR, Rotation.DEG_270);
        createAndAddToTable(-1, -4, TileName.CITY1RSWE, Rotation.DEG_180);
        createAndAddToTable(0, -5, TileName.CITY2WE, Rotation.DEG_90, anton, SOUTH);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("Anton has 12 points for unfinished castle", 12, anton.getCurrentPoints());
    }

    @Test
    public void cloisterWithRoadConnectedToLand() {
        exception.expect(RuntimeException.class);
        createAndAddToTable(1, 0, TileName.CLOISTERR, Rotation.DEG_90, anton, SOUTH);
        createAndAddToTable(0, 1, TileName.CLOISTERR, Rotation.DEG_90, anton, SSE);
    }

    @Test
    public void correctLandWhenCloisterWithRoad() {
        tile_0_0 = Tile.getInstance(0, 0, CITY1RWE);
        tile_1_0 = Tile.getInstance(1, 0, ROAD2NS);
        tile_1_0 = tile_1_0.turnRight(Rotation.DEG_90);
        tile_2_0 = Tile.getInstance(2, 0, CLOISTERR);
        tile_2_0 = tile_2_0.turnRight(Rotation.DEG_90);

        Map<Tile, Set<TileDirection>> expectedRealEstate = new HashMap<>();
        expectedRealEstate.put(tile_0_0, new HashSet<>(Arrays.asList(WWN, EEN, WWS, EES, SOUTH, SSW, SSE)));
        expectedRealEstate.put(tile_1_0, new HashSet<>(Arrays.asList(WWN, EEN, SSE, SSW, SOUTH, WWS, EES, NNE, NNW, NORTH)));
        expectedRealEstate.put(tile_2_0, new HashSet<>(Arrays.asList(WWN, NNW, NORTH, NNE, EEN, EAST,
                EES, SSE, SOUTH, SSW, WWS)));

        createAndAddToTable(1, 0, TileName.ROAD2NS, Rotation.DEG_90);
        createAndAddToTable(2, 0, TileName.CLOISTERR, Rotation.DEG_90, anton, SOUTH);

        ArrayList<RealEstate> antons = new ArrayList<>(manager.getAssets(anton));
        Map<Tile, Set<TileDirection>> antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();

        assertEquals("Anton has specific asset", expectedRealEstate, antonRealEstate);
    }

    @Test
    public void loopedRoadWithCrossroadsFinished() {
        createAndAddToTable(1, 1, ROAD2SW, Rotation.DEG_270);
        createAndAddToTable(2, 1, ROAD2SW, Rotation.DEG_0, anton, SOUTH);
        createAndAddToTable(2, 2, ROAD3, Rotation.DEG_180);
        createAndAddToTable(1, 2, ROAD2SW, Rotation.DEG_180);

        assertEquals("Anton gets 4 points for finished road", 4, anton.getCurrentPoints());
    }

    @Test
    public void cloistersCloseToEachOther_finishCorrectly() {
        createAndAddToTable(0, -1, CITY1RSE, Rotation.DEG_180);
        createAndAddToTable(1, 0, ROAD4, Rotation.DEG_0);
        createAndAddToTable(1, -1, CLOISTERR, Rotation.DEG_0, anton, CENTER);
        createAndAddToTable(0, -2, CITY2NWSR, Rotation.DEG_0);
        createAndAddToTable(2, 0, CITY3SR, Rotation.DEG_90);
        createAndAddToTable(1, -2, ROAD2SW, Rotation.DEG_90);
        createAndAddToTable(2, -2, CLOISTER, Rotation.DEG_0, anton, CENTER);
        createAndAddToTable(2, -1, CITY11NE, Rotation.DEG_90);
        assertEquals("Anton gets 9 points for one finished cloister", 9, anton.getCurrentPoints());
    }

    private RealEstate placeAndUpdate(List<Tile> expected) {
        RealEstate realEstate = null;

        for (Tile tile : expected) {
            simpleTable.placeTile(tile);
            if (tile.hasFollower() && realEstate == null)
                realEstate = RealEstate.getInstance(tile, this.mTable);
            else if (realEstate != null)
                realEstate.update(tile);
        }
        assert (realEstate != null);
        return realEstate;
    }

    private class SimpleTable implements TilesOnTable {
        Set<Tile> placedTiles = new HashSet<>();

        public void placeTile(Tile tile) {
            placedTiles.add(tile);
        }

        @Override
        public Tile getTile(int x, int y) {
            List<Tile> result = placedTiles.stream()
                    .filter(tile -> tile.getCoordinates().equals(new Coordinates(x, y)))
                    .collect(Collectors.toList());
            if (result.size() == 0)
                return Tile.getNullInstance();
            else if (result.size() == 1)
                return result.get(0);

            assert (false);
            return null;
        }

        @Override
        public Tile getNeighbouringTile(int x, int y, TileDirection direction) {
            return getTile(direction.getNeighbourCoordinates(x, y));
        }

        private Tile getTile(Coordinates coordinates) {
            return getTile(coordinates.getX(), coordinates.getY());
        }
    }
}