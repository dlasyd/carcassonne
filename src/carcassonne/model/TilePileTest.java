package carcassonne.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.beans.ExceptionListener;

public class TilePileTest {
    public TilePile tilePile;
    @Rule

    public ExpectedException exception =
            ExpectedException.none();
    @Before
    public void initialize() {
        tilePile = new TilePile();
    }

    @Test
    public void staticFactoryTest() {
        TilePile pile = TilePile.getInstance();
        assertTrue (pile != null);
        TilePile pile2 = TilePile.getInstance();
        assertTrue (pile == pile2);
    }

    @Test
    public void ifTileAddedNumberOfTilesIncreases() {
        assertEquals (0, tilePile.getNumberOfTiles());
        tilePile.addTile(Tile.getInstance());
        assertEquals (1, tilePile.getNumberOfTiles());
    }

    @Test
    public void ifTileDraggedThenNumberOfTilesDecreases() {
        assertEquals (0, tilePile.getNumberOfTiles());
        tilePile.addTile(Tile.getInstance());
        assertEquals (1, tilePile.getNumberOfTiles());
        Tile t2 = tilePile.dragTile();
        assertEquals (0, tilePile.getNumberOfTiles());
    }
	/*
	 * Randomness of a tile being dragged was not tested with JUnit
	 */


    @Test
    public void countRemainingTiles() {
        tilePile.addTile(Tile.getInstance());
        tilePile.addTile(Tile.getInstance());
        tilePile.addTile(Tile.getInstance());
        tilePile.addTile(Tile.getInstance());
        tilePile.dragTile();
        assertEquals ("Number of tiles before +1 = n of t after", 3, tilePile.getNumberOfTiles());
        tilePile.dragTile();
        assertEquals ("Number of tiles before +1 = n of t after", 2, tilePile.getNumberOfTiles());
    }

    @Test
    public void ifDragFromEmptyPileThenRuntimeException() {
        exception.expect(RuntimeException.class);
        tilePile.dragTile();
    }

    @Test
    public void hasTiles() {
        assertEquals ("There are no tiles", tilePile.hasTiles(), false);
        tilePile.addTile(Tile.getInstance());
        assertEquals ("There are tiles", tilePile.hasTiles(), true);
    }
}


















