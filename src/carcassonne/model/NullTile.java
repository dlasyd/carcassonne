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
    public Coordinates getCoordinates() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public int getX() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public int getY() {
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
    public boolean hasCoordinates() {
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
    public Set<TileDirections> getFeatureTileDirections(Feature feature) {
        return null;
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
    public Set<Feature> getFeatures() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Feature getOccupiedFeature() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    void returnFollowerToPlayer() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Player getFollowerOwner() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    Feature getFeature(TileDirections direction) {
        return null;
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

    @Override
    public TileName getName() {
        return null;
    }

    @Override
    public boolean isContinuous(Tile tile, TileDirections direction) {
        return false;
    }

    public Rotation getCurrentRotation() {
        return null;
    }

    private void throwRuntimeException() {
        throw new RuntimeException("The tile is Null");
    }
}
