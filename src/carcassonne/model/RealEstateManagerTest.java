package carcassonne.model;

import com.sun.prism.es2.ES2Graphics;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static carcassonne.model.Rotation.*;
import static carcassonne.model.TileDirections.*;
import static carcassonne.model.TileName.*;
import static org.junit.Assert.*;

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
    public Tile tile_1_0 = Tile.getInstance(1, 0);
    public Tile tile_2_0 = Tile.getInstance(2, 0);
    public Tile tile_3_0 = Tile.getInstance(3, 0);
    public Tile tile_0_1 = Tile.getInstance(0, 1);
    public Tile tile_1_1 = Tile.getInstance(1, 1);
    public Tile tile_2_1 = Tile.getInstance(2, 1);
    public Tile tile_2_m1 = Tile.getInstance(2, -1);
    public Tile tile_1_2 = Tile.getInstance(1, 2);
    public RealEstate realEstate;
    public RealEstate realEstate2;
    public Feature feature = Feature.createFeature(FeatureType.CITY);
    public Table table;
    public RealEstateManager manager;


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

    @Before
    public void setUp() {
        table = new Table();
        manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
    }

    @Test
    public void playerHasRealEstate() {
        table = new Table();
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_1_0 = tile_1_0.placeFollower(anton, EAST);
        realEstate = RealEstate.getInstance(tile_1_0);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        table.setRealEstateManager(manager);

        tile_2_0 = tile_2_0.placeFollower(anton, EAST);
        realEstate2 = RealEstate.getInstance(tile_2_0);
        Set<RealEstate> expectedSet = new HashSet<>();
        expectedSet.add(realEstate);
        expectedSet.add(realEstate2);

        manager.addAsset(anton, realEstate);
        manager.addAsset(anton, realEstate2);
        assertEquals("Player has set of Real estate", expectedSet, manager.getAssets(anton));
    }

    /*
     * Real estate should be created by placing a follower onto a TileDirection of current tile
     */
    @Test
    public void createAsset() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));

        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180);
        placeTile(2, 0, TileName.ROAD2SW, Rotation.DEG_0, anton, WEST);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        Set<RealEstate> resultRealEstate = manager.getAssets(anton);
        Set<Tile> resultingRETiles = new HashSet<>();
        for (RealEstate estate: resultRealEstate) {
            resultingRETiles.addAll(estate.getTileSet());
        }
        assertEquals("Players assets set", expected, resultingRETiles);
    }

    @Test
    public void isPartOfRealEstate() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        tile_1_0 = tile_1_0.turnRight(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2SW));
        placeTile(1, 0, TileName.ROAD2SW, Rotation.DEG_180, anton, EAST);
        placeTile(2, 0, TileName.ROAD2SW, Rotation.DEG_0);
        assertTrue("Part of real estate", manager.isPartOfRealEstate(tile_2_0, WEST));
        assertFalse("Part of real estate", manager.isPartOfRealEstate(tile_2_0, EAST));
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
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_0 = tile_1_0.turnRight(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_2_0 = tile_2_0.turnRight(DEG_180);
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_0_1 = tile_0_1.turnRight(DEG_180);
        tile_2_1 = tile_2_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CITY3));

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, anton, SOUTH);
        placeTile(2, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(0, 1, TileName.CITY2NW,  Rotation.DEG_180, lena, EAST);
        placeTile(2, 1, TileName.CITY2NW,  Rotation.DEG_0);

        placeTile(1, 1, TileName.CITY3, Rotation.DEG_0);

        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(andrey),
                new HashSet<Tile>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(anton),
                new HashSet<Tile>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(lena),
                new HashSet<Tile>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(lena));
        assertEquals("Players have same property set", manager.getAssets(lena), manager.getAssets(andrey));
    }

    @Test
    public void assetUnionThreesomeStepByStep() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_0 = tile_1_0.turnRight(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_2_0 = tile_2_0.turnRight(DEG_180);
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_0_1 = tile_0_1.turnRight(DEG_180);
        tile_2_1 = tile_2_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CITY3));

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, anton, SOUTH);
        placeTile(0, 1, TileName.CITY2NW,  Rotation.DEG_180, lena, EAST);
        placeTile(1, 1, TileName.CITY3,    Rotation.DEG_0);     //first union
        placeTile(2, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(2, 1, TileName.CITY2NW,  Rotation.DEG_0);     //second union

        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(andrey),
                new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(anton),
                new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
        assertTrue("The real estate is correct", RealEstateManager.assetSetContainsRealEstateWithTileSet(manager.getAssets(lena),
                new HashSet<>(Arrays.asList(tile_1_0, tile_2_0, tile_1_1, tile_2_1, tile_0_1))));
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

        assertEquals ("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals ("Anton has 2 real estates", 2, manager.getAssets(anton).size());
    }

    @Test
    public void realEstateChangeOwner2to1() {
        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(CITY1RWE));
        tile_1_0 = tile_1_0.turnRight(DEG_180);
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(CITY2NWR));
        tile_2_0 = tile_2_0.turnRight(DEG_180);
        tile_0_1 = tile_0_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));
        tile_0_1 = tile_0_1.turnRight(DEG_180);
        tile_1_1 = tile_1_1.copyFeatures(TilePile.getReferenceTile(CITY3));
        tile_2_1 = tile_2_1.copyFeatures(TilePile.getReferenceTile(CITY2NW));


        Map<Tile, Set<TileDirections>> expectedRealEstate = new HashMap();
        expectedRealEstate.put(tile_1_0, new HashSet<>(Arrays.asList(SOUTH, SSE, SSW )));
        expectedRealEstate.put(tile_2_0, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_0_1, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_1_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST, EEN, EES, EAST)));
        expectedRealEstate.put(tile_2_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST)));

        placeTile(1, 0, TileName.CITY1RWE, Rotation.DEG_180, andrey, SOUTH);
        placeTile(2, 0, TileName.CITY2NWR, Rotation.DEG_180, anton, SOUTH);
        placeTile(0, 1, TileName.CITY2NW,  Rotation.DEG_180, anton, SOUTH);
        placeTile(1, 1, TileName.CITY3,    Rotation.DEG_0);
        placeTile(2, 1, TileName.CITY2NW,  Rotation.DEG_0);

        Map<Tile, Set<TileDirections>> antonRealEstate;

        ArrayList<RealEstate> antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();

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

        assertEquals ("Finished road that consists of two tiles is 2 points", 2, anton.getCurrentPoints());
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
    public void tileReturnedAfterRealEstateIsFinished() {
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

}