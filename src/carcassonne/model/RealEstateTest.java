package carcassonne.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 04/12/15.
 */
public class RealEstateTest {
    public Tile tile;
    public RealEstate realEstate = new RealEstate();

    public void completeCrossroads(Tile tile) {
        tile.addProperty(new Feature(), TileDirections.EAST);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = new Tile();
        completeCrossroads(tile);
        realEstate.addTile(tile);
    }

    @Test
    public void addTileIncompleteThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile = new Tile(1, 1);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);

        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);
        realEstate.addTile(tile);
    }

    @Test
    public void addTileSameCoordinatesThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(0, 0);
        completeCrossroads(tile1);
        completeCrossroads(tile2);
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

    @Test
    public void addTileDisjointThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(1, 1);
        completeCrossroads(tile1);
        completeCrossroads(tile2);
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

}