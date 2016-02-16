package carcassonne.model;

import carcassonne.model.feature.Feature;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Tile {
    public static Tile getNullInstance() {
        return new NullTile();
    }

    public static Tile getInstance() {
        return new RealTile();
    }

    public static Tile getInstance(TileName tileName) {
        Tile tile = new RealTile(tileName);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        return tile;
    }

    public static Tile getInstance(int x, int y) {
        return new RealTile(x, y);
    }

    public static Tile getInstance(int x, int y, TileName tileName) {
        Tile tile = new RealTile(tileName);
        tile = tile.copyFeatures(TilePile.getReferenceTile(tileName));
        tile = tile.setCoordinates(x, y);
        return tile;
    }

    public abstract boolean isNull();
    public abstract boolean isContinuous(Tile tile, TileDirections direction);
    public abstract boolean featureEqual(Tile roadTile);
    public abstract boolean directionsEqual(Tile referenceTile);
    public abstract Tile    turnRight(Rotation angle);
    public abstract Tile    copyFeatures(Tile referenceTile);

    abstract Tile setCoordinates(int x, int y);
    abstract Tile placeFollower(Player player, Feature feature);
    public abstract Tile placeFollower(Player player, TileDirections direction);
    public abstract Tile returnFollowerToPlayer();
    abstract void addFeature(Feature feature, TileDirections direction);
    public abstract void addFeature(Feature feature, TileDirections... directions);

    //<editor-fold desc="Getters">
    public abstract LinkedHashSet<TileDirections> getFeatureTileDirections(Feature feature);
    public abstract Rotation         getCurrentRotation();
    public abstract Coordinates      getCoordinates();
    public abstract int              getX();
    public abstract int              getY();
    public abstract TileDirections[] getUnoccupiedDirections();
    public abstract TileName         getName();
    public abstract Feature                 getOccupiedFeature();
    public abstract Player                  getFollowerOwner();
    public abstract Feature                 getFeature(TileDirections direction);
    public abstract Set<TileDirections>     getDestinations(TileDirections tileDirection);
    public abstract Set<TileDirections>     getOccupiedFeatureDirections();
    public abstract Set<Feature>            getFeatures();

    public abstract boolean hasFollower();
    public abstract boolean isComplete();
    public abstract boolean hasCoordinates();
    abstract boolean hasCloister();

    public abstract TileDirections getFollowerTileDirection();

    public abstract boolean featureBordersWith(Feature feature, Set<TileDirections> tileDirections);


    //</editor-fold>
}

