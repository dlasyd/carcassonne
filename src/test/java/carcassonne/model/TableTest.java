package carcassonne.model;

import static org.junit.Assert.*;

import carcassonne.model.tile.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

public class TableTest {
    public Table table;

    @Rule
    public ExpectedException exception =
            ExpectedException.none();


    @Before
    public void initialize() {
        table = new Table();

    }

    @Test
    public void staticFactory() {
        Table table1 = Table.getInstance();
        assertTrue (table1 != null);
        Table table2 = Table.getInstance();
        assertTrue (table1 == table2);
    }

    @Test
    public void firstTableInTheCenterOfTable() {
        Tile firstTile = table.getFirstTile();
        assertEquals (0, firstTile.getX());
        assertEquals (0, firstTile.getY());
        assertEquals (firstTile, table.getTile(0, 0));
    }

    @Test
    public void ifPlacedOnOccupiedPositionThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = Tile.getInstance(1, 1);
        Tile tile2 = Tile.getInstance(1, 1);
        table.placeTile(tile1);
        table.placeTile(tile2);
    }

    @Test
    public void placedTilesAmount() {
        Tile tile1 = Tile.getInstance(1, 1, TileName.CLOISTER);
        Tile tile2 = Tile.getInstance(2, 1, TileName.CLOISTER);
        table.placeTile(tile1);
        table.placeTile(tile2);
        assertEquals(3, table.getPlacedTiles().size());
    }

    @Test
    public void confirmedTileParametersAreSavedInTable() {
        HashMap<Coordinates, Tile> passedTiles = new HashMap<>();
        /*
         * Table has an initial tile in it's hashSet and it needs to be placed to passedTiles also
         */
        passedTiles.put(new Coordinates(0, 0), table.getFirstTile());

        Tile tile = Tile.getInstance(1, 0, TileName.CLOISTER);
        passedTiles.put(tile.getCoordinates(), tile);
        table.placeTile(tile);
        assertTrue ("Tiles that were placed on table", passedTiles.equals(table.getPlacedTiles()));
    }

    @Test
    public void getNeighbourByTileDirections() {
        Table table = Table.getInstance();
        Tile expected = Tile.getInstance(1, 0, TileName.CLOISTER);
        table.placeTile(expected);
        table.placeTile(Tile.getInstance(2, 0, TileName.CLOISTER));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(2, 0, TileDirection.WEST));
        table.placeTile(Tile.getInstance(1, 1, TileName.CLOISTER));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(1, 1, TileDirection.NORTH));
        table.placeTile(Tile.getInstance(1, -1, TileName.CLOISTER));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(1, -1, TileDirection.SOUTH));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(0, 0, TileDirection.EAST));
    }

    @Test
    public void getNeighbourNotExistThenNullTile() {
        assertTrue("If neighbour does not exist then return NullTile",
                table.getNeighbouringTile(0, 0, TileDirection.EEN) instanceof NullTile);

    }

    //TODO move to TableTest
    /*
    @Test
    public void addToTableDoNotAddToRealEstate() {
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);

        tile_1_0 = tile_1_0.copyFeatures(TilePile.getReferenceTile(ROAD4));
        tile_2_0 = tile_2_0.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_0 = tile_2_0.turnClockwise(Rotation.DEG_90);
        tile_2_0 = tile_2_0.placeFollower(new Player(), TileDirection.EAST);
        tile_2_m1 = tile_2_m1.copyFeatures(TilePile.getReferenceTile(ROAD2NS));
        tile_2_m1 = tile_2_m1.turnClockwise(Rotation.DEG_90);
        table.placeTile(tile_1_0);
        table.placeTile(tile_2_0);
        realEstate = RealEstate.getInstance(tile_2_0, null);
        manager.addAsset(new Player(), realEstate);
        table.placeTile(tile_2_m1);

        Set<Tile> expected = new HashSet<>(Arrays.asList(tile_1_0, tile_2_0));
        assertEquals("Two tiles are added", expected, realEstate.getTileSet());
    }*/

    //TODO move to tabl
//    @Test
//    public void cloisterWithRoadConnectedToLand() {
//        exception.expect(RuntimeException.class);
//        createAndAddToTable(1, 0, TileName.CLOISTERR, Rotation.DEG_90, anton, SOUTH);
//        createAndAddToTable(0, 1, TileName.CLOISTERR, Rotation.DEG_90, anton, SSE);
//    }


}

























