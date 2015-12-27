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
        throw new RuntimeException("The tile is Null");
    }

    @Override
    int getX() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    int getY() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    void setCoordinates(int x, int y) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    boolean isNoFollower() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    boolean isComplete() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    boolean hasCoordinates() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    boolean hasCloister() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    void placeFollower(Player player, Feature feature) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    void placeFollower(Player player, TileDirections direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    void addFeature(Feature feature, TileDirections direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    void addFeature(Feature feature, TileDirections... directions) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    Set<TileDirections> getDestinations(TileDirections tileDirection) {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Set<TileDirections> getOccupiedFeatureDirections() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Set<Feature> getFeatures() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Feature getOccupiedFeature() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    void returnFollower() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Player getFollowerOwner() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    boolean containsEND() {
        return false;
    }

    @Override
    boolean isCompleteCity() {
        return false;
    }

    @Override
    public TileDirections[] getUnoccupiedDirections() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public void turnRight(Rotation angle) {
        throw new RuntimeException("The tile is Null");

    }

    @Override
    public boolean featureEqual(Tile roadTile) {
        return false;
    }

    @Override
    public boolean directionsEqual(Tile referenceTile) {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public void copyFeatures(Tile referenceTile) {
        throw new RuntimeException("The tile is Null");

    }

    private void throwRuntimeException() {
        throw new RuntimeException("The tile is Null");
    }
}
