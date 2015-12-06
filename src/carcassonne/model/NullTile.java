package carcassonne.model;

import java.util.Set;

/**
 * Created by Andrey on 06/12/15.
 */
public class NullTile extends Tile{
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    Coordinates getCoordinates() {
        return null;
    }

    @Override
    int getX() {
        return 0;
    }

    @Override
    int getY() {
        return 0;
    }

    @Override
    void setCoordinates(int x, int y) {

    }

    @Override
    boolean isNoFollower() {
        return false;
    }

    @Override
    boolean isComplete() {
        return false;
    }

    @Override
    boolean hasCoordinates() {
        return false;
    }

    @Override
    boolean hasCloister() {
        return false;
    }

    @Override
    void placeFollower(Player player, Feature feature) {

    }

    @Override
    void placeFollower(Player player, TileDirections direction) {

    }

    @Override
    void addFeature(Feature feature, TileDirections direction) {

    }

    @Override
    void addFeature(Feature feature, TileDirections... directions) {

    }

    @Override
    Set<TileDirections> getDestinations(TileDirections tileDirection) {
        return null;
    }

    @Override
    Set<TileDirections> getOccupiedFeatureDirections() {
        return null;
    }

    @Override
    Set<Feature> getFeatures() {
        return null;
    }

    @Override
    Feature getOccupiedFeature() {
        return null;
    }

    @Override
    void returnFollower() {

    }
}
