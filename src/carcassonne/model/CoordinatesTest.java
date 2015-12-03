package carcassonne.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 02/12/15.
 */
public class CoordinatesTest {
    public Coordinates coordinates = new Coordinates(0, 0);

    @Test
    public void noRotationByDefault() {
        assertEquals ("Rotation by default", Coordinates.Rotation.DEG_0, coordinates.getRotation());
    }

    @Test
    public void turnRightChangesEnum() {
        coordinates.turnRight();
        assertEquals ("Tile is turned 90 clockwise", Coordinates.Rotation.DEG_90, coordinates.getRotation());
    }

    @Test
    public void turnRightIsCyclical() {
        for (int i = 0; i < 4; i++)
            coordinates.turnRight();
        assertEquals ("Tile is turned clockwise 4 times", Coordinates.Rotation.DEG_0, coordinates.getRotation());
    }
}