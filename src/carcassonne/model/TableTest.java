package carcassonne.model;

import static org.junit.Assert.*;

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
        Tile tile1 = Tile.getInstance(1, 1);
        Tile tile2 = Tile.getInstance(2, 1);
        table.placeTile(tile1);
        table.placeTile(tile2);
        assertEquals(3, table.placedTilesAmount());
    }

    @Test
    public void updateCurrentTileAlwaysDifferent() {
        Tile tile1 = table.getCurrentTile();
        assertEquals ("\nCurrent tile should not for no reason", tile1, table.getCurrentTile());
        assertEquals ("\nCurrent tile should not for no reason", table.getCurrentTile(), table.getCurrentTile());
        table.dragNewCurrentTile();
        assertNotEquals ("\nDragged tile should be a different object", tile1, table.getCurrentTile());
    }

    @Test
    public void confirmedTileParametersAreSavedInTable() {
        HashMap<Coordinates, Tile> passedTiles = new HashMap<>();
        /*
         * Table has an initial tile in it's hashSet and it needs to be placed to passedTiles also
         */
        passedTiles.put(new Coordinates(0, 0), table.getFirstTile());

        Tile tile = Tile.getInstance(1, 0);
        passedTiles.put(tile.getCoordinates(), tile);
        table.placeTile(tile);
        assertTrue ("Tiles that were placed on table", passedTiles.equals(table.getPlacedTiles()));
    }

    @Test
    public void getNeighbourByTileDirections() {
        Table table = Table.getInstance();
        Tile expected = Tile.getInstance(1, 0);
        table.placeTile(expected);
        table.placeTile(Tile.getInstance(2,0));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(2, 0, TileDirections.WEST));
        table.placeTile(Tile.getInstance(1,1));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(1, 1, TileDirections.NORTH));
        table.placeTile(Tile.getInstance(1,-1));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(1, -1, TileDirections.SOUTH));
        assertEquals("Neighbouring tile by tile direction", expected, table.getNeighbouringTile(0, 0, TileDirections.EAST));
    }

    @Test
    public void getNeighbourNotExistThenNullTile() {
        assertTrue("If neighbour does not exist then return NullTile",
                table.getNeighbouringTile(0, 0, TileDirections.EEN) instanceof NullTile);

    }
}

























