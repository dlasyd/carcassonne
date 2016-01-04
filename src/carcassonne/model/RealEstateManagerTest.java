package carcassonne.model;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

import static carcassonne.model.TileDirections.*;
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
    public Feature feature = new Feature();
    public Table table = new Table();
    public RealEstateManager manager = new RealEstateManager(table);

    @Before
    public void setUp() {
        table = new Table();
        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        tile_1_0.placeFollower(anton, EAST);
        realEstate = RealEstate.getInstance(tile_1_0, table);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        //tile_2_0.placeFollower(anton, EAST);
        //realEstate2 = new RealEstate(tile_2_0, table);
    }

    @Test
    public void playerHasRealEstate() {
        tile_2_0.placeFollower(anton, EAST);
        realEstate2 = RealEstate.getInstance(tile_2_0, table);
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
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));

        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        table.placeFollower(anton, WEST);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        Set<RealEstate> resultRealEstate = manager.getAssets(anton);
        Set<Tile> resultingRETiles = new HashSet<>();
        for (RealEstate estate: resultRealEstate) {
            resultingRETiles.addAll(estate.getTileSet());
        }
        assertEquals("Players assets set", expected, resultingRETiles);
    }

    @Test
    public void assetUnionThenOnlyOneRealEstate() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        table.placeFollower(anton, EAST);
        table.placeTile(tile_3_0);
        table.placeFollower(andrey, WEST);


        table.placeTile(tile_2_0);

        assertEquals("Different players have same property list of one shared property", 1, manager.getRealEstateMap().size());
        assertEquals("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
    }

    @Test
    public void assetUnionThreesomeSameTile() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_2_0.turnRight(Rotation.DEG_180);
        tile_0_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_0_1.turnRight(Rotation.DEG_180);
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY3));
        table.placeTile(tile_1_0);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_2_0);
        table.placeFollower(andrey, SOUTH);
        table.placeTile(tile_0_1);
        table.placeFollower(lena, EAST);
        table.placeTile(tile_2_1);

        //tile that unites real estate of three players
        table.placeTile(tile_1_1);

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
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_2_0.turnRight(Rotation.DEG_180);
        tile_0_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_0_1.turnRight(Rotation.DEG_180);
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY3));
        table.placeTile(tile_1_0);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_0_1);
        table.placeFollower(lena, EAST);
        table.placeTile(tile_1_1);  //first union
        table.placeTile(tile_2_0);
        table.placeFollower(andrey, SOUTH);
        table.placeTile(tile_2_1);  //second union

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
    public void doubleUnionWhenTilePlaced() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_1_0.turnRight(Rotation.DEG_90);
        tile_2_m1.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_2_m1.turnRight(Rotation.DEG_180);
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_1.turnRight(Rotation.DEG_90);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_3_0.turnRight(Rotation.DEG_270);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NWR));
        tile_2_0.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        table.placeFollower(anton, EAST);
        table.placeTile(tile_2_m1);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_2_1);
        table.placeFollower(andrey, NORTH);
        table.placeTile(tile_3_0);
        table.placeFollower(andrey, WEST);

        table.placeTile(tile_2_0);      // union tile

        assertEquals ("Players have same property set", manager.getAssets(anton), manager.getAssets(andrey));
        assertEquals ("Anton has 2 real estates", 2, manager.getAssets(anton).size());
    }

    @Test
    public void realEstateChangeOwner2to1() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        Set<RealEstate> expected = new HashSet<>();
        Map<Tile, Set<TileDirections>> expectedRealEstate = new HashMap();
        expectedRealEstate.put(tile_1_0, new HashSet<>(Arrays.asList(SOUTH, SSE, SSW )));
        expectedRealEstate.put(tile_2_0, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_0_1, new HashSet<>(Arrays.asList(SSW, SSE, SOUTH, EES, EEN, EAST)));
        expectedRealEstate.put(tile_1_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST, EEN, EES, EAST)));
        expectedRealEstate.put(tile_2_1, new HashSet<>(Arrays.asList(NNE, NNW, NORTH, WWN, WWS, WEST)));

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NWR));
        tile_2_0.turnRight(Rotation.DEG_180);
        tile_0_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_0_1.turnRight(Rotation.DEG_180);
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY3));
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));

        table.placeTile(tile_1_0);
        table.placeFollower(andrey, SOUTH);
        table.placeTile(tile_2_0);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_0_1);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_1_1);
        table.placeTile(tile_2_1);

        Map<Tile, Set<TileDirections>> antonRealEstate;

        ArrayList<RealEstate> antons = new ArrayList<>(manager.getAssets(anton));
        antonRealEstate = antons.get(0).getTilesAndFeatureTileDirections();

        assertEquals("Anton has specific asset", expectedRealEstate, antonRealEstate);
        assertEquals("Andrey has no assets", false, manager.getPlayerToRealEstateSetMap().containsKey(andrey));

    }

    @Test
    public void realEstateChangeOwner3vs1() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_1_2.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_0_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_0_1.turnRight(Rotation.DEG_90);
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY4));
        tile_2_1.turnRight(Rotation.DEG_270);
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY4));


        table.placeTile(tile_1_0);
        table.placeFollower(andrey, SOUTH);
        table.placeTile(tile_1_2);
        table.placeFollower(anton, NORTH);
        table.placeTile(tile_0_1);
        table.placeFollower(anton, EAST);
        table.placeTile(tile_2_1);
        table.placeFollower(anton, WEST);
        table.placeTile(tile_1_1);

        assertEquals("Andrey has no assets", false, manager.getPlayerToRealEstateSetMap().containsKey(andrey));
        assertEquals("Anton has assets", true, manager.getPlayerToRealEstateSetMap().containsKey(anton));

    }

    @Test
    public void scoreByLength() {
        Set<Integer> expected = new HashSet<>(Collections.singletonList(3));
        Set<Integer> antonPointsSet = null;
        assertEquals("Player real estate worth 3 points", expected, antonPointsSet);
    }

    @Test
    public void smallCastleScore() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY1RWE));
        table.placeTile(tile_1_0);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_1_1);
        assertEquals("4 points for smallest finished castle", 4, anton.getCurrentPoints());
    }

    @Test
    public void diamondCastleScore() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_1_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_1_1.turnRight(Rotation.DEG_90);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        tile_2_0.turnRight(Rotation.DEG_270);
        tile_2_1.copyFeatures(TilePile.getReferenceTile(TileName.CITY2NW));
        table.placeTile(tile_1_0);
        table.placeFollower(anton, SOUTH);
        table.placeTile(tile_1_1);
        table.placeTile(tile_2_0);
        table.placeTile(tile_2_1);
        assertEquals("8 points for smallest finished castle", 8, anton.getCurrentPoints());
    }





    @Test
    public void completeRealEstateSmallRoad() {
        //assertEquals("No incomplete assets");
    }
}