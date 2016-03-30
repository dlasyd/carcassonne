package carcassonne.model.tile;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static carcassonne.model.tile.TileDirection.*;

import static org.junit.Assert.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 08/12/15.
 */
public class TileDirectionTest {
    public TileDirection direction;

    @Test
    public void getEdgeSet() {
        Set<TileDirection> expected = new HashSet<>();
        direction = EAST;
        expected.addAll(Arrays.asList(EAST, EEN, EES));
        assertEquals("TileDirection should return Set of TileDirection of edges", expected, direction.getEdge());
    }
}