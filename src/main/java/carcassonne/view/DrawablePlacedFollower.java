package carcassonne.view;

import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Rotation;

import java.awt.*;
import java.util.Arrays;

/**
 * Contains information required to display placed followers in a game window
 */
public class DrawablePlacedFollower {
    private final Coordinates coordinates;
    private final double[] xyMultipliers;     //for 0 rotation
    private final Color color;
    private final Rotation rotation;

    public DrawablePlacedFollower(Coordinates coordinates, double[] xyMultipliers, Color color, Rotation rotation) {
        this.coordinates = coordinates;
        this.xyMultipliers = xyMultipliers.clone();
        this.color = color;
        this.rotation = rotation;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public double[] getXyMultipliers() {
        return xyMultipliers;
    }

    public Color getColor() {
        return color;
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode() + color.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrawablePlacedFollower))
            return false;
        DrawablePlacedFollower other = (DrawablePlacedFollower) obj;
        return coordinates.equals(other.coordinates) && Arrays.equals(xyMultipliers, other.xyMultipliers)
                && color.equals(other.color);
    }

    @Override
    public String toString() {
        return coordinates.toString() + " " + Arrays.toString(xyMultipliers) + " " + color.toString();
    }

    public int getTileX() {
        return coordinates.getX();
    }

    public int getTileY() {
        return coordinates.getY();
    }


}
