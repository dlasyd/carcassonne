package carcassonne.model;

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
    abstract Tile placeFollower(Player player, TileDirections direction);
    abstract Tile returnFollowerToPlayer();
    abstract void addFeature(Feature feature, TileDirections direction);
    abstract void addFeature(Feature feature, TileDirections... directions);

    //<editor-fold desc="Getters">
    public abstract Set<TileDirections> getFeatureTileDirections(Feature feature);
    public abstract Rotation         getCurrentRotation();
    public abstract Coordinates      getCoordinates();
    public abstract int              getX();
    public abstract int              getY();
    public abstract TileDirections[] getUnoccupiedDirections();
    public abstract TileName         getName();
    abstract Feature                 getOccupiedFeature();
    abstract Player                  getFollowerOwner();
    abstract Feature                 getFeature(TileDirections direction);
    abstract Set<TileDirections>     getDestinations(TileDirections tileDirection);
    abstract Set<TileDirections>     getOccupiedFeatureDirections();
    public abstract Set<Feature>            getFeatures();

    abstract boolean isNoFollower();
    abstract boolean isComplete();
    public abstract boolean hasCoordinates();
    abstract boolean hasCloister();

    public abstract TileDirections getFollowerTileDirection();

    //</editor-fold>
}

