package carcassonne.model.tile;

import carcassonne.model.Player;
import carcassonne.model.Feature.Feature;

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
    public abstract boolean isContinuous(Tile tile, TileDirection direction);
    public abstract boolean featureEqual(Tile roadTile);
    public abstract boolean directionsEqual(Tile referenceTile);
    public abstract Tile    turnRight(Rotation angle);
    public abstract Tile    copyFeatures(Tile referenceTile);

    public abstract Tile setCoordinates(int x, int y);
    abstract Tile placeFollower(Player player, Feature feature);
    public abstract Tile placeFollower(Player player, TileDirection direction);
    public abstract Tile returnFollowerToPlayer();
    abstract void addFeature(Feature feature, TileDirection direction);
    public abstract void addFeature(Feature feature, TileDirection... directions);

    //<editor-fold desc="Getters">
    public abstract LinkedHashSet<TileDirection> getFeatureTileDirections(Feature feature);
    public abstract Rotation         getCurrentRotation();
    public abstract Coordinates      getCoordinates();
    public abstract int              getX();
    public abstract int              getY();
    public abstract TileDirection[] getUnoccupiedDirections();
    public abstract TileName         getName();
    public abstract Feature                 getOccupiedFeature();
    public abstract Player                  getFollowerOwner();
    public abstract Feature                 getFeature(TileDirection direction);
    public abstract Set<TileDirection>     getDestinations(TileDirection tileDirection);
    public abstract Set<TileDirection>     getOccupiedFeatureDirections();
    public abstract Set<Feature>            getFeatures();

    public abstract boolean hasFollower();
    public abstract boolean isComplete();
    public abstract boolean hasCoordinates();
    abstract boolean hasCloister();

    public abstract TileDirection getFollowerTileDirection();

    public abstract boolean featureBordersWith(Feature feature, Set<TileDirection> tileDirections);


    //</editor-fold>
}

