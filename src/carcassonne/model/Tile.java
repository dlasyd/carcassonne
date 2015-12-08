package carcassonne.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Tile {
    public static Tile getNullInstance() {
        return new NullTile();
    }

    public abstract boolean isNull();
    abstract Coordinates getCoordinates();
    abstract int getX();
    abstract int getY();
    abstract void setCoordinates(int x, int y);
    abstract boolean isNoFollower();
    abstract boolean isComplete();
    abstract boolean hasCoordinates();
    abstract boolean hasCloister();
    abstract void placeFollower(Player player, Feature feature);
    abstract void placeFollower(Player player, TileDirections direction);
    abstract void addFeature(Feature feature, TileDirections direction);
    abstract void addFeature(Feature feature, TileDirections... directions);
    abstract Set<TileDirections> getDestinations(TileDirections tileDirection);
    abstract Set<TileDirections> getOccupiedFeatureDirections();
    abstract Set<Feature> getFeatures();
    abstract Feature getOccupiedFeature();
    abstract void returnFollower();

    public static Tile getInstance() {
        return new RealTile();
    }

    public static Tile getInstance(int x, int y) {
        return new RealTile(x, y);
    }

    /*
     * This method is created to make creating test Tiles easier. Should not be used in real Tile creation
     * that will be developed later.
     */
    public abstract TileDirections[] getUnoccupiedDirections();
}

