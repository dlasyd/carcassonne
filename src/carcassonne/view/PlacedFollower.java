package carcassonne.view;

import carcassonne.model.Coordinates;
import carcassonne.model.Feature;


/**
 * Describes on which feature the follower is placed
 */
public class PlacedFollower {
    private Coordinates coordinates;
    private Feature feature;

    public PlacedFollower(Coordinates coordinates, Feature feature) {
        this.coordinates = coordinates;
        this.feature = feature;
    }

    public Coordinates getCoordinates() {
        return coordinates;
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
        return this.coordinates.equals(placedFollower.coordinates)
                && this.feature == placedFollower.feature;
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
