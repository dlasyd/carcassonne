package carcassonne.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This test class tests a lot of real game situations to make sure that RealEstate class behaves properly
 */
public class RealEstateTest {
    public Tile tile;
    public Tile tile_0_0, tile_1_0, tile_2_0, tile_1_1, tile_3_0, tile_4_0, tile_5_0, completeCrossroads_0_0, tile_6_0;
    public RealEstate realEstate;
    public Table table;

    /*
     * w|w
     * -o-
     * w|w
     */
    public void completeCrossroads(Tile tile) {
        tile.addFeature(new Feature(), TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WEST);
        tile.addFeature(new Feature(), TileDirections.SOUTH);
        tile.addFeature(new Feature(), TileDirections.NORTH);
        tile.addFeature(new Feature(), TileDirections.CENTER);
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addFeature(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addFeature(new Feature(), TileDirections.EES, TileDirections.SSE);
    }

    /*
     * www
     * ---
     * www
     */
    public void simpleRoad(Tile tile) {
        tile.addFeature(new Feature(), TileDirections.WWN, TileDirections.NNW, TileDirections.NORTH, TileDirections.NNE, TileDirections.EEN);
        tile.addFeature(new Feature(), TileDirections.WEST, TileDirections.EAST);
        tile.addFeature(new Feature(), TileDirections.WWS, TileDirections.SSW, TileDirections.SOUTH, TileDirections.SSE, TileDirections.EES);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        completeCrossroads_0_0 = Tile.getInstance(0, 0);
        table = new Table();
        completeCrossroads(completeCrossroads_0_0);
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
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = Tile.getInstance();
        completeCrossroads(tile);
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
        completeCrossroads(tile1);
        completeCrossroads(tile2);
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

    @Test
    public void addTileDisjointThenRuntimeException() {
        exception.expect(RuntimeException.class);
        completeCrossroads(tile_0_0);
        completeCrossroads(tile_1_1);
        realEstate.addTile(tile_0_0);
        realEstate.addTile(tile_1_1);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        completeCrossroads(tile_0_0);
        new RealEstate(tile_0_0, table);
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        completeCrossroads(tile_0_0);
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        completeCrossroads(tile_1_0);
        RealEstate realEstate = new RealEstate(tile_0_0, table);
        realEstate.addTile(tile_1_0);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile_0_0);
        expectedSet.add(tile_1_0);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnUnconnectedRealEstateFeature() {
        completeCrossroads(tile_0_0);
        completeCrossroads(tile_1_0);
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
        completeCrossroads(tile_0_0);
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
        completeCrossroads(tile_0_0);
        tile_0_0.placeFollower(new Player(), TileDirections.EAST);
        simpleRoad(tile_1_0);
        simpleRoad(tile_2_0);
        realEstate = new RealEstate(tile_0_0, table);
        realEstate.addTile(tile_1_0);
        realEstate.addTile(tile_2_0);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_0_0, tile_1_0, tile_2_0));
        assertTrue("Three tiles are added", expected.equals(realEstate.getTileSet()));
    }

    @Test
    public void addThirdTileToPropertyFollowerOnSecond() {
        completeCrossroads(tile_1_0);
        simpleRoad(tile_2_0);
        tile_2_0.placeFollower(new Player(), TileDirections.EAST);
        simpleRoad(tile_3_0);
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
        completeCrossroads(tile_1_0);
        completeCrossroads(tile_3_0);
        table.placeTile(tile_1_0);
        table.placeTile(tile_3_0);
        simpleRoad(tile_2_0);
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
        completeCrossroads(tile_1_0);
        completeCrossroads(tile_5_0);
        simpleRoad(tile_2_0);
        simpleRoad(tile_3_0);
        simpleRoad(tile_4_0);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_4_0);
        table.placeTile(tile_5_0);

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
        completeCrossroads(tile_1_0);
        completeCrossroads(tile_6_0);
        simpleRoad(tile_2_0);
        simpleRoad(tile_3_0);
        simpleRoad(tile_4_0);
        simpleRoad(tile_5_0);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeTile(tile_3_0);
        table.placeTile(tile_5_0);
        table.placeTile(tile_6_0);

        tile_4_0.placeFollower(new Player(), TileDirections.EAST);
        RealEstate realEstate = new RealEstate(tile_4_0, table);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_3_0, tile_4_0, tile_5_0, tile_6_0));
        assertEquals("Five tiles are added to real estate", expected, realEstate.getTileSet());
    }
}