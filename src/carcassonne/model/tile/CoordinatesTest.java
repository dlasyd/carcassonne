package carcassonne.model.tile;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 02/12/15.
 */
public class CoordinatesTest {
    public Coordinates coordinates = new Coordinates(0, 0);
    public HashSet<Coordinates> coordinatesSet = new HashSet<>();
    public HashSet<Coordinates> expectedSet = new HashSet<>();

    @Test
    public void noRotationByDefault() {
        assertEquals ("Rotation by default", Rotation.DEG_0, coordinates.getRotation());
    }

    @Test
    public void aroundSetOfOne() {
        coordinatesSet.add(new Coordinates(0, 0));
        expectedSet.add(new Coordinates(1, 0));
        expectedSet.add(new Coordinates(-1, 0));
        expectedSet.add(new Coordinates(0, 1));
        expectedSet.add(new Coordinates(0, -1));
        assertTrue ("Set of coordinates around coordinate", expectedSet.equals(Coordinates.getAround(coordinatesSet)));
    }

    @Test
    public void aroundSetOfTwo() {
        coordinatesSet.add(new Coordinates(0, 0));
        coordinatesSet.add(new Coordinates(1, 0));
        int[][] coords = {{-1, 0}, {2, 0}, {0, 1}, {1, 1} , {0, -1}, {1, -1}};
        for (int[] c: coords) {
            expectedSet.add(new Coordinates(c[0], c[1]));
        }
        assertTrue ("Set of coordinates around coordinate", expectedSet.equals(Coordinates.getAround(coordinatesSet)));
    }
}
