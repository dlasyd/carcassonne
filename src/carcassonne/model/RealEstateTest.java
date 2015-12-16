package carcassonne.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public RealEstate realEstate;
    public Table table;



    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        completeCrossroads_0_0 = Tile.getInstance(0, 0);
        table = new Table();
        completeCrossroads_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        completeCrossroads_0_0.placeFollower(new Player(), TileDirections.WEST);
        realEstate = new RealEstate(completeCrossroads_0_0, table);
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
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance();
        tile.copyFeatures(TilePile.getReferenceTile(ROAD4));
        realEstate.addTile(tile);
    }

    @Test
    public void addTileIncompleteThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance(1 ,1);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);

        tile.addFeature(new Feature(), TileDirections.CENTER);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);
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
        RealEstate realEstate = new RealEstate(tile_0_0, table);
        realEstate.addTile(nullTile);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        new RealEstate(tile_0_0, table);
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        RealEstate realEstate = new RealEstate(tile_0_0, table);
        realEstate.addTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnUnconnectedRealEstateFeature() {
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = new RealEstate(tile_0_0, table);
        tile_1_0.placeFollower(new Player(), TileDirections.EAST);

        realEstate.addTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnConnectedRealEstateFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirections.WEST);
        RealEstate realEstate = new RealEstate(tile_0_0, table);
        tile_1_0.placeFollower(new Player(), TileDirections.EAST);

        realEstate.addTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addThirdTileToPropertyFollowerOnFirst() {
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_1_0.turnRight(Rotation.DEG_90);
        tile_2_0.turnRight(Rotation.DEG_90);
        realEstate = new RealEstate(tile_0_0, table);
        realEstate.addTile(tile_1_0);
        realEstate.addTile(tile_2_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_0_0, tile_1_0, tile_2_0));
        assertTrue("Three tiles are added", expected.equals(realEstate.getTileSet()));
    }

    @Test
    public void addThirdTileToPropertyFollowerOnSecond() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        realEstate = new RealEstate(tile_2_0, table);
        realEstate.addTile(tile_1_0);
        realEstate.addTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertTrue("Three tiles are added", expected.equals(realEstate.getTileSet()));
    }

    /*
     * tile_1_0 and tile_3_0 are on the table before we put tile_2_0 and create a real estate
     */
    @Test
    public void createPropertyFromMiddleTile() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        table.placeTile(tile_1_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_2_0);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0.placeFollower(new Player(), TileDirections.EAST);

        RealEstate realEstate = new RealEstate(tile_2_0, table);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertTrue("Three tiles are added", expected.equals(realEstate.getTileSet()));

    }

    /*
     * tile_1_0, tile_2_0, tile_4_0, tile_5_0 are on the table before we put tile_3_0 and create real estate
     * Real estate in test is a 5 - tile long horizontal road
     */
    @Test
    public void createPropertyFromManyPreviouslyPlacedTiles() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_5_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_4_0);
        table.placeTile(tile_5_0);

        table.placeTile(tile_3_0);
        tile_3_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = new RealEstate(tile_3_0, table);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0, tile_5_0));
        assertEquals("Five tiles are added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * Same as above but three tiles to the left when creating real estate (tile_0_0 does not count)
     */
    @Test
    public void createPropertyFromPreviousThreeToTheLeft() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_6_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0.turnRight(Rotation.DEG_90);
        tile_5_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_5_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_5_0);
        table.placeTile(tile_6_0);

        table.placeTile(tile_4_0);

        tile_4_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = new RealEstate(tile_4_0, table);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0, tile_5_0, tile_6_0));
        assertEquals("Five tiles are added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * Finding adjacent tiles when they are placed vertically
     */
    @Test
    public void verticalTest() {
        tile_0_1.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_0_2.copyFeatures(TilePile.getReferenceTile(ROAD4));
        table.placeTile(tile_0_2);
        table.placeTile(tile_0_1);
        tile_0_1.placeFollower(new Player(), SOUTH);
        RealEstate realEstate = new RealEstate(tile_0_1, table);
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
        tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1));
        tile_1_m1.copyFeatures(TilePile.getReferenceTile(CITY3));
        tile_1_m1.turnRight(Rotation.DEG_90);
        tile_1_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));
        tile_1_m2.turnRight(Rotation.DEG_90);
        tile_2_m1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2.copyFeatures(TilePile.getReferenceTile(CITY2NW));

        table.placeTile(tile_1_m1);
        table.placeTile(tile_1_m2);
        table.placeTile(tile_1_0);
        tile_1_0.placeFollower(new Player(), TileDirections.NORTH);
        RealEstate realEstate = new RealEstate(tile_1_0, table);
        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_m1, tile_1_m2));
        assertEquals("Six tiles are added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void createPropertyFromComplexPreviousTilePlacement() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1));
        tile_1_m1.copyFeatures(TilePile.getReferenceTile(CITY3));
        tile_1_m1.turnRight(Rotation.DEG_90);
        tile_1_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));
        tile_1_m2.turnRight(Rotation.DEG_90);
        tile_2_m1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_2_m2.turnRight(Rotation.DEG_180);
        tile_3_m2.copyFeatures(TilePile.getReferenceTile(CITY2WE));

        table.placeTile(tile_1_m1);
        table.placeTile(tile_1_m2);
        table.placeTile(tile_2_m1);
        table.placeTile(tile_2_m2);
        table.placeTile(tile_3_m2);
        table.placeTile(tile_1_0);
        tile_1_0.placeFollower(new Player(), TileDirections.NORTH);
        RealEstate realEstate = new RealEstate(tile_1_0, table);
        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2));
        assertEquals("Six tiles are added to real estate", expected, realEstate.getTileSet());
    }

    @Test
    public void loopedRoadProperty() {
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_1.turnRight(Rotation.DEG_180);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_2_1.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_2_1.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        table.placeTile(tile_1_1);
        table.placeTile(tile_2_0);
        table.placeTile(tile_2_1);

        tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = new RealEstate(tile_1_0, table);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_1_1, tile_2_0, tile_2_1));
        assertEquals("Road loop added to real estate", expected, realEstate.getTileSet());
    }

    /*
     * When tile is placed on the table, it should be added to all real estate object that are appropriate.
     * This is relevant for next few tests
     */
    @Test
    public void addingTileToRealEstate() {
        RealEstateManager manager = new RealEstateManager();
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = new RealEstate(tile_2_0, table);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addToTableDoNotAddToRealEstate() {
        RealEstateManager manager = new RealEstateManager();
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_m1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_m1.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = new RealEstate(tile_2_0, table);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_2_m1);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateNotNearCreationTile() {
        RealEstateManager manager = new RealEstateManager();
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = new RealEstate(tile_1_0, table);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_3_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0));
        assertEquals("Three tiles are added", expected, realEstate.getTileSet());
    }

    @Test
    public void addingTileToRealEstateFarFromCreationTile() {
        RealEstateManager manager = new RealEstateManager();
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0.placeFollower(new Player(), TileDirections.EAST);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_0.turnRight(Rotation.DEG_90);
        tile_4_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_4_0.turnRight(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_3_0);
        realEstate = new RealEstate(tile_1_0, table);
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
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_2.turnRight(Rotation.DEG_180);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2.turnRight(Rotation.DEG_90);

        tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));

        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_2.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_2.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = new RealEstate(tile_1_0, table);

        RealEstateManager manager = new RealEstateManager();
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
        tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_270);
        tile_1_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_2.turnRight(Rotation.DEG_180);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_3_2.turnRight(Rotation.DEG_90);

        tile_1_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_3_1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));

        tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);
        tile_2_2.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_2.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        tile_1_0.placeFollower(new Player(), EAST);
        RealEstate realEstate = new RealEstate(tile_1_0, table);

        RealEstateManager manager = new RealEstateManager();
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

}