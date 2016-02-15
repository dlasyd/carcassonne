package carcassonne.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static carcassonne.model.Rotation.*;
import static org.junit.Assert.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 19/01/16.
 */
public class TilePlacingHelperTest {
    public Table table = new Table();
    public TilePlacingHelper helper = new TilePlacingHelper(table);

    @Test
    public void cloisterWithNoRoadCoordinates() {
        Map<Coordinates, Set<Rotation>> expected = new HashMap<>();
        Util.addLinkedSetElement(expected,  new Coordinates(0, 1), DEG_0, DEG_90, DEG_180, DEG_270);
        helper.setPlacedTiles(table.getPlacedTiles());
        Tile tile = Tile.getInstance(TileName.CLOISTER);
        helper.update(tile);
        assertEquals("Set of coordinates", expected, helper.getCoordinatesToRotationMap());
    }

    @Test
    public void city1() {
        Map<Coordinates, Set<Rotation>> expected = new HashMap<>();
        Util.addLinkedSetElement(expected, new Coordinates(0, 1), DEG_90, DEG_180, DEG_270);
        Util.addLinkedSetElement(expected, new Coordinates(0, -1), DEG_180);
        helper.setPlacedTiles(table.getPlacedTiles());
        Tile tile = Tile.getInstance(TileName.CITY1);
        helper.update(tile);
        assertEquals("Set of coordinates", expected, helper.getCoordinatesToRotationMap());
    }

    @Test
    public void road3() {
        Map<Coordinates, Set<Rotation>> expected = new HashMap<>();
        Util.addLinkedSetElement(expected, new Coordinates(-1, 0), DEG_0, DEG_180, DEG_270);
        Util.addLinkedSetElement(expected, new Coordinates(1, 0), DEG_0, DEG_90, DEG_180);
        Util.addLinkedSetElement(expected, new Coordinates(0, 1), DEG_0);
        helper.setPlacedTiles(table.getPlacedTiles());
        Tile tile = Tile.getInstance(TileName.ROAD3);
        helper.update(tile);
        assertEquals("Set of coordinates", expected, helper.getCoordinatesToRotationMap());
    }

    @Test
    public void city4() {
        Map<Coordinates, Set<Rotation>> expected = new HashMap<>();
        Util.addLinkedSetElement(expected, new Coordinates(0, -1), DEG_0, DEG_90, DEG_180, DEG_270);
        helper.setPlacedTiles(table.getPlacedTiles());
        Tile tile = Tile.getInstance(TileName.CITY4);
        helper.update(tile);
        assertEquals("Set of coordinates", expected, helper.getCoordinatesToRotationMap());
    }
}