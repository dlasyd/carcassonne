package carcassonne.model.realestate;

import carcassonne.model.*;
import carcassonne.model.Feature.Feature;
import carcassonne.model.Feature.FeatureType;
import carcassonne.model.tile.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static carcassonne.model.tile.Rotation.*;
import static carcassonne.model.tile.TileDirection.*;
import static carcassonne.model.tile.TileName.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 10/12/15.
 */
public class RealEstateManagerTest {
    public Player anton = new Player("anton", null);
    public Player andrey = new Player("andrey", null);
    public Player lena = new Player("lena", null);
    public Tile tile_0_m1 = Tile.getInstance(0, -1);
    public Tile tile_m1_m1 = Tile.getInstance(-1, -1);
    public Tile tile_1_0 = Tile.getInstance(1, 0);
    public Tile tile_2_0 = Tile.getInstance(2, 0);
    public Tile tile_0_1 = Tile.getInstance(0, 1);
    public Tile tile_1_1 = Tile.getInstance(1, 1);
    public Tile tile_2_1 = Tile.getInstance(2, 1);
    public Tile tile_2_m1 = Tile.getInstance(2, -1);
    public Table table;
    public RealEstateManager manager;
    public RealEstate mockRE, otherMockRE;
    private RealEstate.ImmutableRealEstate mockIRE, otherMockIRE;


    public void placeTile(int x, int y, TileName tileName, Rotation rotation) {
        Tile tile = Tile.getInstance(x, y, tileName)
                .turnClockwise(rotation);
        table.placeTile(tile);

    }

    public void placeTile(int x, int y, TileName tileName, Rotation rotation, Player player, TileDirection tileDirection) {
        Tile tile = Tile.getInstance(x, y, tileName)
                .turnClockwise(rotation)
                .placeFollower(player, tileDirection);
        table.placeTile(tile);

    }

    @Before
    public void setUp() {
        table = new Table();
        manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        mockRE = mock(RealEstate.class);
        otherMockRE = mock(RealEstate.class);
        mockIRE = mock(RealEstate.ImmutableRealEstate.class);
        otherMockIRE = mock(RealEstate.ImmutableRealEstate.class);
        connectMockREtoIRE();
    }

    @Test
    public void addRealEstate() {
        manager.addAsset(anton, mockRE);
        assertEquals(new HashSet<>(Collections.singletonList(mockRE)), manager.getAssets(anton));
    }

    @Test
    public void playerHasTwoRealEstates() {
        Set<RealEstate> expectedSet = new HashSet<>();
        expectedSet.add(mockRE);
        expectedSet.add(otherMockRE);

        manager.addAsset(anton, mockRE);
        manager.addAsset(anton, otherMockRE);
        assertEquals("Player has set of Real estate", expectedSet, manager.getAssets(anton));
    }

    /*
     * Real estate should be created by placing a follower onto a TileDirection of current tile
     */
    @Test
    public void createAsset() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2SW);

        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180);
        placeTile(2, 0, TileName.ROAD2SW, Rotation.DEG_0, anton, WEST);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        Set<RealEstate> resultRealEstate = manager.getAssets(anton);
        Set<Tile> resultingRETiles = new HashSet<>();
        for (RealEstate estate : resultRealEstate) {
            resultingRETiles.addAll(estate.getTileSet());
        }
        assertEquals("Players assets set", expected, resultingRETiles);
    }

    @Test
    public void isPartOfRealEstate() {
        tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, ROAD2SW);
        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180, anton, EAST);
        placeTile(2, 0, TileName.ROAD2SW, Rotation.DEG_0);
        assertTrue("Part of real estate", manager.isPartOfRealEstate(tile_2_0, WEST));
        assertFalse("Part of real estate", manager.isPartOfRealEstate(tile_2_0, EAST));
    }

    @Test
    public void isPartOfRealEstate_castle() {
        tile_0_m1 = Tile.getInstance(0, -1, CITY2NW)
                .turnClockwise(DEG_270);
        tile_m1_m1 = Tile.getInstance(-1, -1, CITY2NW)
                .turnClockwise(DEG_90);
        placeTile(0, -1, TileName.CITY2NW, Rotation.DEG_270, anton, SOUTH);
        table.placeTile(tile_m1_m1);
        assertTrue("Part of real estate", manager.isPartOfRealEstate(tile_m1_m1, EAST));
        assertFalse("Part of real estate", manager.isPartOfRealEstate(tile_m1_m1, WEST));
    }

    @Test
    public void assetUnionThenOnlyOneRealEstate() {
        placeTile(1, 0, ROAD2SW, DEG_180, anton, EAST);
        placeTile(3, 0, ROAD2SW, DEG_0, andrey, WEST);
        placeTile(2, 0, ROAD2NS, DEG_90);

        assertEquals("Different players have same property list of one shared property", 1, manager.getRealEstateMap().size());
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
    }


    @Test
    public void assetUnionThreesomeSameTile() {
        tile_1_0 = Tile.getInstance(1, 0, CITY1RWE)
                .turnClockwise(DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, CITY1RWE)
                .turnClockwise(DEG_180);
        tile_0_1 = Tile.getInstance(0, 1, CITY2NW)
                .turnClockwise(DEG_180);
        tile_2_1 = Tile.getInstance(2, 1, CITY2NW);
        tile_1_1 = Tile.getInstance(1, 1, CITY3);

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, anton, SOUTH);
        placeTile(2, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(0, 1, TileName.CITY2NW, Rotation.DEG_180, lena, EAST);
        placeTile(2, 1, TileName.CITY2NW, Rotation.DEG_0);

        placeTile(1, 1, TileName.CITY3, Rotation.DEG_0);

        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(lena));
        assertEquals("Players have same property set", manager.getAssets(lena), manager.getAssets(andrey));
    }

    @Test
    public void assetUnionThreesomeStepByStep() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_0 = tile_1_0.turnClockwise(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_2_0 = tile_2_0.turnClockwise(DEG_180);
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_0_1 = tile_0_1.turnClockwise(DEG_180);
        tile_2_1 = tile_2_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CITY3));

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, anton, SOUTH);
        placeTile(0, 1, TileName.CITY2NW, Rotation.DEG_180, lena, EAST);
        placeTile(1, 1, TileName.CITY3, Rotation.DEG_0);     //first union
        placeTile(2, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(2, 1, TileName.CITY2NW, Rotation.DEG_0);     //second union

        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(lena));
        assertEquals("Players have same property set", manager.getAssets(lena), manager.getAssets(andrey));
    }

    @Test
    public void doubleUnionWhenTilePlaced() {
        placeTile(1, 0, ROAD2NS, DEG_90, anton, EAST);
        placeTile(2, -1, CITY1RWE, DEG_180, anton, SOUTH);
        placeTile(2, 1, ROAD2SW, DEG_90, andrey, NORTH);
        placeTile(3, 0, CITY2NW, DEG_270, andrey, WEST);
        placeTile(2, 0, CITY2NWR, DEG_90);

        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals("Anton has 2 real estates", 2, manager.getAssets(anton).size());
    }

    @Test
    public void realEstateChangeOwner2to1() {
        tile_1_0 = Tile.getInstance(1, 0, CITY1RWE)
                .turnClockwise(DEG_180);
        tile_2_0 = Tile.getInstance(2, 0, CITY2NWR)
                .turnClockwise(DEG_180);
        tile_0_1 = Tile.getInstance(0, 1, CITY2NW)
                .turnClockwise(DEG_180);
        tile_1_1 = Tile.getInstance(1, 1, CITY3);
        tile_2_1 = Tile.getInstance(2, 1, CITY2NW);


        Map<Tile, Set<TileDirection>> expectedRealEstate = new HashMap<>();
        expectedRealEstate.put(tile_1_0, new HashSet<>(Arrays.asList(SOUTH, SSE, SSW)));
        expectedRealEstate.put(tile_2_0, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_0_1, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_1_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST, EEN, EES, EAST)));
        expectedRealEstate.put(tile_2_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST)));

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(2, 0, TileName.CITY2NWR, Rotation.DEG_180, anton, SOUTH);
        placeTile(0, 1, TileName.CITY2NW, Rotation.DEG_180, anton, SOUTH);
        placeTile(1, 1, TileName.CITY3, Rotation.DEG_0);
        placeTile(2, 1, TileName.CITY2NW, Rotation.DEG_0);

        Map<Tile, Set<TileDirection>> antonRealEstate;

        ArrayList<RealEstate> antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();

        boolean debug = expectedRealEstate.equals(antonRealEstate);
        assertEquals("Anton has specific asset", expectedRealEstate, antonRealEstate);
        assertEquals("Andrey has no assets", false, manager.getPlayerToRealEstateSetMap().containsKey(andrey));

    }

    @Test
    public void realEstateChangeOwner3vs1() {
        placeTile(1, 0, CITY1RWE, DEG_180, andrey, SOUTH);
        placeTile(1, 2, CITY1RWE, DEG_0, anton, NORTH);
        placeTile(0, 1, CITY1RWE, DEG_90, anton, EAST);
        placeTile(2, 1, CITY4, DEG_270, anton, WEST);
        placeTile(1, 1, CITY4, DEG_0);

        assertEquals("Andrey has no assets", false, manager.getPlayerToRealEstateSetMap().containsKey(andrey));
        assertEquals("Anton has assets", true, manager.getPlayerToRealEstateSetMap().containsKey(anton));

    }

    @Test
    public void smallCastleScore() {
        placeTile(1, 0, CITY1RWE, DEG_180, anton, SOUTH);
        placeTile(1, 1, CITY1RWE, DEG_0);
        assertEquals("4 points for smallest finished castle", 4, anton.getCurrentPoints());
    }

    @Test
    public void diamondCastleScore() {
        placeTile(1, 0, CITY2NW, DEG_180, anton, SOUTH);
        placeTile(1, 1, CITY2NW, DEG_90);
        placeTile(2, 0, CITY2NW, DEG_270);
        placeTile(2, 1, CITY2NW, DEG_0);
        assertEquals("8 points for smallest finished castle", 8, anton.getCurrentPoints());
    }

    @Test
    public void completeRealEstateSmallRoad() {
        placeTile(1, 0, ROAD4, DEG_0);
        placeTile(2, 0, ROAD4, DEG_0, anton, WEST);

        assertEquals("Finished road that consists of two tiles is 2 points", 2, anton.getCurrentPoints());
    }

    @Test
    public void completeCloister9Points() {
        placeTile(1, 1, CITY1, DEG_0);
        placeTile(2, 1, CITY1, DEG_0);
        placeTile(3, 1, CITY1, DEG_0);
        placeTile(1, 2, CITY1, DEG_270);
        placeTile(2, 2, CLOISTER, DEG_0, anton, CENTER);
        placeTile(3, 2, CITY1, DEG_90);
        placeTile(1, 3, CITY1, DEG_180);
        placeTile(2, 3, CITY1, DEG_180);
        placeTile(3, 3, CITY1, DEG_180);
        assertEquals("9 points for finished cloister", 9, anton.getCurrentPoints());
    }

    @Test
    public void inCompleteCloister9Points() {
        placeTile(1, 1, CITY1, DEG_0);
        placeTile(2, 1, CITY1, DEG_0);
        placeTile(3, 1, CITY1, DEG_0);
        placeTile(1, 2, CITY1, DEG_270);
        placeTile(2, 2, CLOISTER, DEG_0, anton, CENTER);
        placeTile(3, 2, CITY1, DEG_90);
        placeTile(1, 3, CITY1, DEG_180);
        placeTile(2, 3, CITY1, DEG_180);
        manager.addPointsForUnfinishedRealEstate();
        assertEquals("9 points for finished cloister", 8, anton.getCurrentPoints());
    }

    @Test
    public void lessFollowersAfterPlaced() {
        placeTile(1, 1, CITY1, DEG_0, anton, NORTH);
        assertEquals("Anton has 6 followers after placing one", 6, anton.getNumberOfFollowers());
    }

    @Test
    public void followerReturnedAfterRealEstateIsFinished() {
        placeTile(1, 1, CITY1, DEG_180, anton, SOUTH);
        placeTile(1, 2, CITY1, DEG_0);
        assertEquals("Anton has 7 followers after city is finished", 7, anton.getNumberOfFollowers());
    }

    @Test
    public void roadUnion() {
        placeTile(1, 1, ROAD4, DEG_0, anton, EAST);
        placeTile(3, 1, ROAD4, DEG_0, andrey, WEST);
        placeTile(2, 1, ROAD2NS, DEG_90);
        assertEquals("Anton has 7 followers after city is finished", 7, anton.getNumberOfFollowers());
        assertEquals("Andrey has 7 followers after city is finished", 7, andrey.getNumberOfFollowers());
    }

    @Test
    public void separatedLandThreeCities() {
        placeTile(0, -1, TileName.CITY1RSE, Rotation.DEG_180, anton, EAST);
        placeTile(0, 1, TileName.CITY1, Rotation.DEG_180, andrey, NORTH);
        placeTile(0, 2, TileName.CITY1RWE, Rotation.DEG_0, anton, WWN);
        manager.addPointsForUnfinishedRealEstate();

        assertEquals("Anton has 6 points", 6, anton.getCurrentPoints());
        assertEquals("Andrey has 3 points", 3, andrey.getCurrentPoints());
    }

    @Test
    public void isPartOfRealEstate_doesNotChangeRealEstate() {
        placeTile(0, -1, CITY2WES, DEG_90);
        placeTile(0, 1, CITY2WES, DEG_0);
        placeTile(0, -2, CITY2NWR, DEG_180);
        placeTile(1, -1, ROAD2SW, DEG_270, anton, EAST);
        placeTile(1, -2, CITY3, DEG_0);
        placeTile(2, -1, CITY3R, DEG_90);
        placeTile(-1, -2, CITY2NWSR, DEG_0);

        Tile tile_1_0 = Tile.getInstance(1, 0, ROAD2SW)
                .turnClockwise(DEG_90);

        table.placeTile(tile_1_0);

        Tile tile_1_m1 = Tile.getInstance(1, -1, ROAD2SW)
                .turnClockwise(DEG_270)
                .placeFollower(anton, EAST);
        tile_2_m1 = Tile.getInstance(2, -1, CITY3R)
                .turnClockwise(DEG_90);
        Tile tile_0_0 = Tile.getInstance(0, 0, CITY1RWE);




        Map<Tile, Set<TileDirection>> expectedRealEstate = new HashMap<>();
        expectedRealEstate.put(tile_1_m1, new HashSet<>(Arrays.asList(SOUTH, EAST)));
        expectedRealEstate.put(tile_2_m1, new HashSet<>(Collections.singletonList(WEST)));
        expectedRealEstate.put(tile_1_0, new HashSet<>(Arrays.asList(NORTH, WEST)));
        expectedRealEstate.put(tile_0_0, new HashSet<>(Arrays.asList(EAST, WEST)));

        ArrayList<RealEstate> antons;
        Map<Tile, Set<TileDirection>> antonRealEstate;

        assertTrue("Part of real estate", manager.isPartOfRealEstate(tile_1_0, NORTH));
        antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();
        assertEquals("RealEstate unchanged", expectedRealEstate, antonRealEstate);

        assertFalse(manager.isPartOfRealEstate(tile_1_0, NNW));
        antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();
        assertEquals("RealEstate unchanged", expectedRealEstate, antonRealEstate);

        assertFalse(manager.isPartOfRealEstate(tile_1_0, SOUTH));
        antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();
        assertEquals("RealEstate unchanged", expectedRealEstate, antonRealEstate);
    }

    // TODO move to RealEstateManagerTest
    /*
    @Test
    public void followerOnOccupiedRealEstateThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0 = tile_1_0.turnClockwise(Rotation.DEG_180);
        tile_3_0 = tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0 = tile_2_0.turnClockwise(Rotation.DEG_90);

        createAndAddToTable(1, 0, TileName.ROAD2SW, Rotation.DEG_180, new Player(), EAST);
        createAndAddToTable(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
        createAndAddToTable(3, 0, TileName.ROAD2SW, Rotation.DEG_0, new Player(), WEST);

    }

    */
//    //TODO ? move to realEstateManager
//    @Test
//    public void sameWhenUnion() {
//        Player andrey = new Player();
//        Player anton = new Player();
//
//        createAndAddToTable(1, 0, TileName.ROAD2NS, Rotation.DEG_90, andrey, EAST);
//        createAndAddToTable(3, 0, TileName.ROAD2NS, Rotation.DEG_90, anton, EAST);
//        createAndAddToTable(2, 0, TileName.ROAD2NS, Rotation.DEG_90);
//        assertEquals("Players should have same real estate object", manager.getAssets(anton), manager.getAssets(andrey));
//    }


    private void connectMockREtoIRE() {
        when(mockRE.getImmutableRealEstate()).thenReturn(mockIRE);
        when(mockIRE.getRealEstate()).thenReturn(mockRE);
        when(otherMockRE.getImmutableRealEstate()).thenReturn(otherMockIRE);
        when(otherMockIRE.getRealEstate()).thenReturn(otherMockRE);
    }
}