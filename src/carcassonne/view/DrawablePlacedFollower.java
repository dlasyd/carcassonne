package carcassonne.view;

import carcassonne.model.Coordinates;

import java.awt.*;

/**
 * Contains information required to display placed followers in a game window
 */
public class DrawablePlacedFollower {
    private Coordinates coordinates;
    private double[] xyMultipliers;
    private Color color;

    public DrawablePlacedFollower(Coordinates coordinates, double[] xyMultipliers, Color color) {
        this.coordinates = coordinates;
        this.xyMultipliers = xyMultipliers;
        this.color = color;
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

    @Override
    public int hashCode() {
        return coordinates.hashCode() + color.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrawablePlacedFollower))
            return false;
        DrawablePlacedFollower other = (DrawablePlacedFollower) obj;
        if (coordinates.equals(other.coordinates) && xyMultipliers.equals(other.xyMultipliers)
                && color.equals(other.color))
            return true;
        else
            return false;
    }

}
