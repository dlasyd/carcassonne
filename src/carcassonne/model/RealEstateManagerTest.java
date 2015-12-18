package carcassonne.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 10/12/15.
 */
public class RealEstateManagerTest {
    public Player anton = new Player();
    public Player andrey = new Player();
    public Tile tile_1_0 = Tile.getInstance(1, 0);
    public Tile tile_2_0 = Tile.getInstance(2, 0);
    public Tile tile_3_0 = Tile.getInstance(3, 0);
    public RealEstate realEstate;
    public RealEstate realEstate2;
    public Feature feature = new Feature();
    public Table table = new Table();
    public RealEstateManager manager = new RealEstateManager(table);

    @Before
    public void setUp() {
        table = new Table();
        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        tile_1_0.placeFollower(anton, TileDirections.EAST);
        realEstate = new RealEstate(tile_1_0, table);
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD4));
        tile_2_0.placeFollower(anton, TileDirections.EAST);
        realEstate2 = new RealEstate(tile_2_0, table);
    }

    @Test
    public void playerHasRealEstate() {
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
        table.placeFollower(anton, TileDirections.WEST);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        Set<RealEstate> resultRealEstate = manager.getAssets(anton);
        Set<Tile> resultingRETiles = new HashSet<>();
        for (RealEstate estate: resultRealEstate) {
            resultingRETiles.addAll(estate.getTileSet());
        }
        assertEquals("Players assets set", expected, resultingRETiles);
    }


    @Test
    public void assetUnion() {
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_1_0.turnRight(Rotation.DEG_180);
        tile_3_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2SW));
        tile_2_0.copyFeatures(TilePile.getReferenceTile(TileName.ROAD2NS));
        tile_2_0.turnRight(Rotation.DEG_90);

        table.placeTile(tile_1_0);
        table.placeFollower(anton, TileDirections.EAST);
        table.placeTile(tile_3_0);
        table.placeFollower(andrey, TileDirections.WEST);

        Set<RealEstate> antonAssets = manager.getAssets(anton);
        assertEquals("Different players have same property list of one shared property", manager.getAssets(anton),
                manager.getAssets(andrey));
    }

}