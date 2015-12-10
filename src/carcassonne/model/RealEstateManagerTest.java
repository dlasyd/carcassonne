package carcassonne.model;

import org.junit.Before;
import org.junit.Test;

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
    public RealEstateManager manager = new RealEstateManager();
    public Tile tile_1_0 = Tile.getInstance(1, 0);
    public Tile tile_2_0 = Tile.getInstance(2, 0);
    public RealEstate realEstate;
    public RealEstate realEstate2;
    public Feature feature = new Feature();
    public Table table;

    /*
     *   |
     * - o -
     *   |
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

    @Before
    public void setUp() {
        table = new Table();
        completeCrossroads(tile_1_0);
        tile_1_0.placeFollower(anton, TileDirections.EAST);
        realEstate = new RealEstate(tile_1_0, table);
        completeCrossroads(tile_2_0);
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

}