package carcassonne.view;

import carcassonne.model.Coordinates;
import carcassonne.model.Feature;

import java.util.Arrays;

/**
 * Describes the position of placed follower on tile
 */
public class PlacedFollower {
    private Coordinates coordinates;
    private Feature feature;
    private double[] xyMultiplier;

    public PlacedFollower(Coordinates coordinates, double[] xyMultiplier) {
        this.coordinates = coordinates;
        this.xyMultiplier = xyMultiplier;
    }

    public PlacedFollower(Coordinates coordinates, Feature feature) {
        this.coordinates = coordinates;
        this.feature = feature;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public double[] getXyMultiplier() {
        return xyMultiplier;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlacedFollower))
            return false;
        PlacedFollower placedFollower = (PlacedFollower) obj;
        coordinates.equals(placedFollower.coordinates);
        Arrays.equals(this.xyMultiplier, placedFollower.xyMultiplier);
        if (this.coordinates.equals(placedFollower.coordinates)
                /*Arrays.equals(this.xyMultiplier, placedFollower.xyMultiplier)
                /*&& this.feature == placedFollower.feature*/)
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode() * 3;
    }

    @Override
    public String toString() {
        return coordinates.toString() + " ";
    }

}
