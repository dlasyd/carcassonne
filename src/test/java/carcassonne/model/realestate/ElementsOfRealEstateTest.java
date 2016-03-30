package carcassonne.model.realestate;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;
import carcassonne.model.tile.TileName;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static carcassonne.model.tile.TileDirection.*;
import static org.junit.Assert.*;

public class ElementsOfRealEstateTest {
    ElementsOfRealEstate element;
    Tile tile;
    TileDirection direction;
    private final Tile tile2 = Tile.getInstance(2, 2, TileName.CITY1RSW);

    @Before
    public void setUp() {
        element = new ElementsOfRealEstate();
        tile = Tile.getInstance(1, 1, TileName.CITY1);
        direction = SOUTH;
    }

    @Test
    public void elementOfRealEstateCanBeAdded() {
        element = element.add(tile, direction);
        assertTrue(element.contains(tile));

        assertFalse(element.contains(Tile.getInstance(1, 2, TileName.CITY1RSE)));
    }

    @Test(expected = RuntimeException.class)
    public void addingSameTile_thenRuntimeException() {
        element = element
                .add(tile, direction)
                .add(tile, direction);
    }

    @Test
    public void addSecondTile() {
        element = element.add(tile, direction).add(tile2, direction);

        assertTrue(element.contains(tile));
        assertTrue(element.contains(tile2));
    }

    @Test
    public void testGetTileSet() {
        element = element.add(tile, direction);

        assertEquals(new HashSet<>(Collections.singletonList(direction)), element.getTileDirectionSet(tile));
    }

    @Test(expected = RuntimeException.class)
    public void getTileDirectionSet_ofNotAddedTile_thenRuntimeException() {
        element.add(tile, direction);
        element.getTileDirectionSet(tile2);
    }

    @Test
    public void testAddTileAndThreeTileDirections() {
        element = element.add(tile, CENTER, EAST, WEST);

        assertEquals(new HashSet<>(Arrays.asList(CENTER, EAST, WEST)), element.getTileDirectionSet(tile));
    }

    @Test(expected = RuntimeException.class)
    public void exceptionAddingSetWithDuplicate() {
        element = element.add(tile, direction);
        HashMap<Tile, Set<TileDirection>> tileSetHashMap = new HashMap<>();
        tileSetHashMap.put(tile2, new HashSet<>(Arrays.asList(CENTER, EAST)));
        tileSetHashMap.put(tile, new HashSet<>(Arrays.asList(NORTH, WEST, EAST)));
        element = element.addAll(tileSetHashMap);
    }
}

























