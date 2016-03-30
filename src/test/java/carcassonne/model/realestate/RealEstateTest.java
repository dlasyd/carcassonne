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
    public Tile tile_0_0, tile_1_0, tile_2_0, tile_1_1, tile_3_0, tile_4_0, tile_5_0, tile_6_0;
    public Tile tile_1_m1, tile_1_m2, tile_2_m1, tile_2_m2, tile_3_m2, tile_0_1, tile_0_2, tile_2_1, tile_1_2, tile_3_2;
    public Tile tile_2_2, tile_3_1, tile_1_3, tile_1_4;
    public RealEstate realEstate;
    public TilesOnTable mTable;
    public SimpleTable simpleTable;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private Table mockTable;

    @Before
    public void setUp() {
        simpleTable = new SimpleTable();
        mTable = simpleTable;
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
        RealEstate realEstate = RealEstate.getInstance(tile_0_0, mTable);
        realEstate.addFirstTile(nullTile);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        tile_0_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        RealEstate.getInstance(tile_0_0, mTable);
    }

    @Test
    public void createRealEstate() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        when(mockTable.getNeighbouringTile(any(Integer.class), any(Integer.class), any(TileDirection.class)))
                .thenReturn(Tile.getNullInstance());
        RealEstate realEstate = RealEstate.getInstance(tile_1_0, mockTable);
        assertEquals(new HashSet<>(Collections.singletonList(tile_1_0)), realEstate.getTileSet());
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = Tile.getInstance(2, 0, ROAD4);

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
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = Tile.getInstance(2, 0, ROAD4)
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

    @Test
    public void addThirdTileToPropertyFollowerOnFirst() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS).turnClockwise(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS).turnClockwise(Rotation.DEG_90);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_3_0);

        assertEquals("Three tiles are added", new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void addThirdTileToPropertyFollowerOnSecond() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);

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
                .turnClockwise(Rotation.DEG_90)
                .placeFollower(new Player(), TileDirection.EAST);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_3_0, tile_2_0);
        assertEquals("Three tiles are added", new HashSet<>(expected), placeAndUpdate(expected).getTileSet());

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
                .turnClockwise(Rotation.DEG_90);
        tile_4_0 = Tile.getInstance(4, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90)
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
                .turnClockwise(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_5_0 = Tile.getInstance(5, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_6_0 = Tile.getInstance(6, 0, ROAD4);
        tile_4_0 = Tile.getInstance(4, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90)
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
                .turnClockwise(Rotation.DEG_90);
        tile_1_m2 = Tile.getInstance(1, -2, CITY2WE)
                .turnClockwise(Rotation.DEG_90);
        tile_1_0 = Tile.getInstance(1, 0, CITY1).placeFollower(new Player(), TileDirection.NORTH);

        tile_2_m1 = Tile.getInstance(2, -1, CITY2NW);
        tile_2_m2 = Tile.getInstance(2, -2, CITY2NW);
        List<Tile> expected = Arrays.asList(tile_1_m1, tile_1_m2, tile_1_0);
        assertEquals(new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void createPropertyFromComplexPreviousTilePlacement() {
        tile_1_m1 = Tile.getInstance(1, -1, CITY3)
                .turnClockwise(Rotation.DEG_90);
        tile_1_m2 = Tile.getInstance(1, -2, CITY2WE)
                .turnClockwise(Rotation.DEG_90);
        tile_2_m1 = Tile.getInstance(2, -1, CITY2NW);
        tile_2_m2 = Tile.getInstance(2, -2, CITY2NW)
                .turnClockwise(Rotation.DEG_180);
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
                .turnClockwise(Rotation.DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2SW);
        tile_2_1 = Tile.getInstance(2, 1, ROAD2SW)
                .turnClockwise(Rotation.DEG_90);
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(Rotation.DEG_270)
                .placeFollower(new Player(), EAST);


        List<Tile> expected = Arrays.asList(tile_1_1, tile_2_0, tile_2_1, tile_1_0);
        assertEquals("Road loop added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void addingTileToRealEstateFarFromCreationTile() {
        tile_3_0 = Tile.getInstance(3, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_1_0 = Tile.getInstance(1, 0, ROAD4)
                .placeFollower(new Player(), TileDirection.EAST);
        tile_4_0 = Tile.getInstance(4, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);


        List<Tile> expected = Arrays.asList(tile_3_0, tile_2_0, tile_1_0, tile_4_0);
        assertEquals("Three tiles are added",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void addingTileToRealEstateLoopedRoadProperty() {
        /*
         * 3x3 road square cycle
         */
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(Rotation.DEG_270)
                .placeFollower(new Player(), EAST);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2SW);
        tile_3_1 = Tile.getInstance(3, 1, ROAD2NS);
        tile_3_2 = Tile.getInstance(3, 2, ROAD2SW)
                .turnClockwise(Rotation.DEG_90);
        tile_2_2 = Tile.getInstance(2, 2, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_1_2 = Tile.getInstance(1, 2, ROAD2SW)
                .turnClockwise(Rotation.DEG_180);
        tile_1_1 = Tile.getInstance(1, 1, ROAD2NS);


        List<Tile> expected = Arrays.asList(tile_1_0, tile_2_0, tile_3_0,
                tile_3_1, tile_3_2, tile_2_2, tile_1_2, tile_1_1);
        assertEquals("Road loop added to real estate",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void tilesAddedToCloister() {
        tile_1_0 = Tile.getInstance(1, 0, CITY1RWE);
        tile_1_1 = Tile.getInstance(1, 1, CLOISTER)
                .placeFollower(new Player(), TileDirection.CENTER);
        tile_0_1 = Tile.getInstance(0, 1, CLOISTER);
        tile_2_0 = Tile.getInstance(2, 0, CLOISTER);
        tile_1_2 = Tile.getInstance(1, 2, CLOISTER);

        List<Tile> expected = Arrays.asList(tile_1_0, tile_1_1, tile_0_1, tile_2_0, tile_1_2);

        assertEquals("Cloister has correct tiles",
                new HashSet<>(expected), placeAndUpdate(expected).getTileSet());
    }

    @Test
    public void checkTileDirectionsOfRealEstate() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(Rotation.DEG_180)
                .placeFollower(new Player(), EAST);
        tile_3_0 = Tile.getInstance(3, 0, ROAD2SW);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);

        Map<Tile, Set<TileDirection>> expected = new LinkedHashMap<>();
        expected.put(tile_1_0, new HashSet<>(Arrays.asList(NORTH, EAST)));
        expected.put(tile_3_0, new HashSet<>(Arrays.asList(WEST, SOUTH)));
        expected.put(tile_2_0, new HashSet<>(Arrays.asList(WEST, EAST)));

        Map<Tile, Set<TileDirection>> realResult = placeAndUpdate(new ArrayList<>(expected.keySet()))
                .getTilesAndFeatureTileDirections();
        assertEquals("Tiles and tile directions", expected, realResult);
    }

    @Test
    public void testEquality() {
        tile_0_1 = Tile.getInstance(0, 1, ROAD4)
                .placeFollower(new Player(), SOUTH);
        tile_0_2 = Tile.getInstance(0, 2, ROAD4);
        List<Tile> list1 = Arrays.asList(tile_0_1, tile_0_2);
        realEstate = placeAndUpdate(list1);

        simpleTable.clear();

        Tile otherTile_0_1 = Tile.getInstance(0, 1, ROAD4)
                .placeFollower(new Player(), SOUTH);
        Tile otherTile_0_2 = Tile.getInstance(0, 2, ROAD4);
        List<Tile> list2 = Arrays.asList(otherTile_0_1, otherTile_0_2);
        RealEstate otherRealEstate = placeAndUpdate(list1);

        assertEquals(realEstate,otherRealEstate);
    }

    @Test
    public void roadIsFinishedWhenTwoEnds() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD4);
        tile_2_0 = Tile.getInstance(2, 0, CITY1RWE)
                .placeFollower(new Player(), WEST);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_0, tile_2_0));
        assertFalse("Player has no unfinished real estate", realEstate.isFinished());
    }

    @Test
    public void landNoFinishedCity() {
        tile_1_2 = Tile.getInstance(1, 2, CITY1)
                .placeFollower(new Player(), SOUTH);
        realEstate = placeAndUpdate(tile_1_2);
        assertFalse("Land is not finished", realEstate.isFinished());
    }

    @Test
    public void landFinishedAndUnfinishedCity_countPoints() {
        tile_1_1 = Tile.getInstance(1, 1, CITY1).turnClockwise(Rotation.DEG_180);
        tile_1_2 = Tile.getInstance(1, 2, CITY1).placeFollower(new Player(), SOUTH);
        tile_1_3 = Tile.getInstance(1, 3, CITY1).turnClockwise(Rotation.DEG_180);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_1, tile_1_2, tile_1_3));
        assertEquals(3, realEstate.getPoints());
    }

    @Test
    public void landFinishedCity_countPoints() {
        tile_1_0 = Tile.getInstance(1, 0, CLOISTER);
        tile_1_1 = Tile.getInstance(1, 1, CITY1).turnClockwise(Rotation.DEG_180);
        tile_1_2 = Tile.getInstance(1, 2, CITY1).placeFollower(new Player(), SOUTH);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_0, tile_1_1, tile_1_2));
        assertEquals(3, realEstate.getPoints());
    }

    @Test
    public void land2FinishedCities_countPoints() {
        tile_1_1 = Tile.getInstance(1, 1, CITY1).turnClockwise(Rotation.DEG_180);
        tile_1_2 = Tile.getInstance(1, 2, CITY1).placeFollower(new Player(), SOUTH);
        tile_1_3 = Tile.getInstance(1, 3, CITY1).turnClockwise(Rotation.DEG_180);
        tile_1_4 = Tile.getInstance(1, 4, CITY1);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_1, tile_1_2, tile_1_3, tile_1_4));
        assertEquals(6, realEstate.getPoints());
    }

    @Test
    public void landWith2FinishedCitiesRussianG_countPoints() {
        tile_1_2 = Tile.getInstance(1, 2, CITY11NE).placeFollower(new Player(), SOUTH);
        tile_1_1 = Tile.getInstance(1, 1, CITY1).turnClockwise(Rotation.DEG_180);
        tile_2_2 = Tile.getInstance(2, 2, CITY1).turnClockwise(Rotation.DEG_270);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_2, tile_1_1, tile_2_2));
        assertEquals(6, realEstate.getPoints());
    }

    @Test
    public void castleWithOneShield_countPoints() {
        tile_1_1 = Tile.getInstance(1, 1, CITY1)
                .turnClockwise(Rotation.DEG_180).placeFollower(new Player(), SOUTH);
        tile_1_2 = Tile.getInstance(1, 2, CITY2NWS);
        tile_0_2 = Tile.getInstance(0, 2, CITY1).turnClockwise(Rotation.DEG_90);

        realEstate = placeAndUpdate(Arrays.asList(tile_1_1, tile_1_2, tile_0_2));
        assertEquals("Three tile castle with one shield is 8 points", 8, realEstate.getPoints());
    }

    @Test
    public void twoCloistersNextToEachOtherArePossible_countPoints() {
        Player anton = new Player();
        createAndAddToTable(1, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        createAndAddToTable(2, 1, TileName.CLOISTER, Rotation.DEG_0, anton, CENTER);
        assertEquals("Anton has 5 followers", 5, anton.getNumberOfFollowers());
    }

    @Test
    public void bigUnfinishedCastleGivesALotOfPoints_countPoints() {
        createAndAddToTable(0, -1, TileName.CITY11WE, Rotation.DEG_90);
        createAndAddToTable(0, -2, TileName.CITY2WES, Rotation.DEG_90);
        createAndAddToTable(0, -3, TileName.CITY4, Rotation.DEG_0);
        createAndAddToTable(-1, -3, TileName.CITY2NW, Rotation.DEG_90);
        createAndAddToTable(1, -3, TileName.CITY2NWR, Rotation.DEG_0);
        createAndAddToTable(0, -4, TileName.CITY3R, Rotation.DEG_90);
        createAndAddToTable(1, -4, TileName.CITY2NWSR, Rotation.DEG_270);
        createAndAddToTable(-1, -4, TileName.CITY1RSWE, Rotation.DEG_180);

        realEstate = placeAndUpdate(Tile.getInstance(0, -5, CITY2WE)
                .turnClockwise(Rotation.DEG_90)
                .placeFollower(new Player(), SOUTH));
        assertEquals("Anton has 12 points for unfinished castle", 12, realEstate.getPoints());
    }

    @Test
    public void loopedRoadWithCrossroadsFinished_countPoints() {
        createAndAddToTable(1, 1, ROAD2SW, Rotation.DEG_270);
        realEstate = placeAndUpdate(Tile.getInstance(2, 1, ROAD2SW).placeFollower(new Player(), SOUTH));
        realEstate.update(createAndAddToTable(2, 2, ROAD3, Rotation.DEG_180));
        realEstate.update(createAndAddToTable(1, 2, ROAD2SW, Rotation.DEG_180));

        assertEquals("4 points for finished road", 4, realEstate.getPoints());
    }

    @Test
    public void cloistersCloseToEachOther_finishCorrectly_countPoints() {
        createAndAddToTable(0, -1, CITY1RSE, Rotation.DEG_180);
        createAndAddToTable(1, 0, ROAD4, Rotation.DEG_0);
        realEstate = placeAndUpdate(Tile.getInstance(1, -1, CLOISTERR).placeFollower(new Player(), CENTER));
        realEstate.update(createAndAddToTable(0, -2, CITY2NWSR, Rotation.DEG_0));
        realEstate.update(createAndAddToTable(2, 0, CITY3SR, Rotation.DEG_90));
        realEstate.update(createAndAddToTable(1, -2, ROAD2SW, Rotation.DEG_90));
        realEstate.update(createAndAddToTable(2, -2, CLOISTER, Rotation.DEG_0));
        realEstate.update(createAndAddToTable(2, -1, CITY11NE, Rotation.DEG_90));

        assertEquals("9 points for one finished cloister", 8, realEstate.getPoints());
    }

    @Test
    public void correctLandWhenCloisterWithRoad() {
        tile_0_0 = Tile.getInstance(0, 0, CITY1RWE);
        tile_1_0 = Tile.getInstance(1, 0, ROAD2NS)
                .turnClockwise(Rotation.DEG_90);
        tile_2_0 = Tile.getInstance(2, 0, CLOISTERR)
                .turnClockwise(Rotation.DEG_90)
                .placeFollower(new Player(), SOUTH);

        Map<Tile, Set<TileDirection>> expectedRealEstate = new HashMap<>();
        expectedRealEstate.put(tile_0_0, new HashSet<>(
                Arrays.asList(WWN, EEN, WWS, EES, SOUTH, SSW, SSE)));
        expectedRealEstate.put(tile_1_0, new HashSet<>(
                Arrays.asList(WWN, EEN, SSE, SSW, SOUTH, WWS, EES, NNE, NNW, NORTH)));
        expectedRealEstate.put(tile_2_0, new HashSet<>(
                Arrays.asList(WWN, NNW, NORTH, NNE, EEN, EAST, EES, SSE, SOUTH, SSW, WWS)));

        Map<Tile, Set<TileDirection>> antonRealEstate = placeAndUpdate(
                Arrays.asList(tile_0_0, tile_1_0, tile_2_0)).getTilesAndFeatureTileDirections();

        assertEquals("Anton has specific asset", expectedRealEstate, antonRealEstate);
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

    private RealEstate placeAndUpdate(Tile tile) {
        return placeAndUpdate(Collections.singletonList(tile));
    }

    private Tile createAndAddToTable(int x, int y, TileName tileName, Rotation rotation) {
        Tile tile = Tile.getInstance(x, y, tileName)
                .copyFeatures(TilePile.getReferenceTile(tileName))
                .turnClockwise(rotation);
        simpleTable.placeTile(tile);
        return tile;
    }

    private Tile createAndAddToTable
            (int x, int y, TileName tileName, Rotation rotation, Player player, TileDirection tileDirection) {
        Tile tile = Tile.getInstance(x, y, tileName)
                .copyFeatures(TilePile.getReferenceTile(tileName))
                .turnClockwise(rotation)
                .placeFollower(player, tileDirection);
        simpleTable.placeTile(tile);
        return tile;
    }

    private class SimpleTable implements TilesOnTable {
        Set<Tile> placedTiles = new HashSet<>();

        public void placeTile(Tile tile) {
            placedTiles.add(tile);
        }

        public void clear() {
            placedTiles.clear();
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

            throw new RuntimeException("Function did not finish correctly");
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