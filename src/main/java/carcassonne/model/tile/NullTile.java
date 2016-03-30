package carcassonne.model.tile;

import carcassonne.model.Player;
import carcassonne.model.Feature.Feature;

import java.util.LinkedHashSet;
import java.util.Set;

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
    public Tile setCoordinates(int x, int y) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public boolean hasFollower() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public boolean isComplete() {
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
    public TileDirection getFollowerTileDirection() {
        return null;
    }

    @Override
    public boolean featureBordersWith(Feature feature, Set<TileDirection> tileDirections) {
        return false;
    }

    @Override
    Tile placeFollower(Player player, Feature feature) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Tile placeFollower(Player player, TileDirection direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    void addFeature(Feature feature, TileDirection direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public void addFeature(Feature feature, TileDirection... directions) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public LinkedHashSet<TileDirection> getFeatureTileDirections(Feature feature) {
        return null;
    }

    @Override
    public Set<TileDirection> getDestinations(TileDirection tileDirection) {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Set<TileDirection> getOccupiedFeatureDirections() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Set<Feature> getFeatures() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Feature getOccupiedFeature() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Tile returnFollowerToPlayer() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Player getFollowerOwner() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Feature getFeature(TileDirection direction) {
        return null;
    }

    @Override
    public TileDirection[] getUnoccupiedDirections() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Tile turnClockwise(Rotation angle) {
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
    public Tile copyFeatures(Tile referenceTile) {
        throw new RuntimeException("The tile is Null");

    }

    @Override
    public TileName getName() {
        return null;
    }

    @Override
    public boolean isContinuous(Tile tile, TileDirection direction) {
        return false;
    }

    public Rotation getCurrentRotation() {
        return null;
    }

    private void throwRuntimeException() {
        throw new RuntimeException("The tile is Null");
    }
}
