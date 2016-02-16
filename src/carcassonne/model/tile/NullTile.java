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
    public TileDirections getFollowerTileDirection() {
        return null;
    }

    @Override
    public boolean featureBordersWith(Feature feature, Set<TileDirections> tileDirections) {
        return false;
    }

    @Override
    Tile placeFollower(Player player, Feature feature) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Tile placeFollower(Player player, TileDirections direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    void addFeature(Feature feature, TileDirections direction) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public void addFeature(Feature feature, TileDirections... directions) {

        throw new RuntimeException("The tile is Null");
    }

    @Override
    public LinkedHashSet<TileDirections> getFeatureTileDirections(Feature feature) {
        return null;
    }

    @Override
    public Set<TileDirections> getDestinations(TileDirections tileDirection) {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Set<TileDirections> getOccupiedFeatureDirections() {
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
    public Feature getFeature(TileDirections direction) {
        return null;
    }

    @Override
    public TileDirections[] getUnoccupiedDirections() {
        throw new RuntimeException("The tile is Null");
    }

    @Override
    public Tile turnRight(Rotation angle) {
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
