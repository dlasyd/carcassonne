package carcassonne.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 06/12/15.
 */
public class RealTile extends Tile {
    private Coordinates coordinates;
    private boolean noFollower = true;
    private Feature occupiedFeature = null;
    private Follower follower;
    private HashMap<Feature,Set<TileDirections>> featureToTileDirections = new HashMap<>();
    private HashMap<TileDirections, Feature> propertyMap = new HashMap<>();

    /*
     * A feature can "connect" one TileDirections with the other. For example, a road from EAST to WEST
     */
    private HashMap<TileDirections, Set<TileDirections>> propertyConnectionMap = new HashMap<>();

    RealTile() {}

    RealTile(int x, int y) {
        setCoordinates(x, y);
    }

    void turnRight() {
        coordinates.turnRight();
    }

    void setCoordinates(int x, int y) {
        coordinates = new Coordinates (x, y);
    }

    int getX() {
        return coordinates.getX();
    }

    int getY() {
        return coordinates.getY();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public Coordinates getCoordinates() {
        if (coordinates == null)
            throw new RuntimeException("Tile has no coordinates");
        return coordinates;
    }

    public boolean isNoFollower() {
        return noFollower;
    }

    public void placeFollower(Player player, Feature feature) {
        noFollower = false;
        occupiedFeature = feature;
        follower = new Follower(player);
        player.placeFollower();
    }

    public void placeFollower(Player player, TileDirections direction) {
        if (propertyMap.get(direction) == null)
            throw new RuntimeException("Cannot place follower using tileDirection because there is no corresponding feature");
        placeFollower(player, propertyMap.get(direction));
    }

    public Set<TileDirections> getDestinations(TileDirections dir) {
        return propertyConnectionMap.get(dir);
    }

    public void addFeature(Feature feature, TileDirections direction) {
        checkIfDirectionIsNotOccupied(direction);

        featureToTileDirections.put(feature, new HashSet<>(Arrays.asList(new TileDirections[]{direction})));
        propertyMap.put(direction, feature);

        if (direction == TileDirections.CENTER) {
            propertyConnectionMap.put(direction, new HashSet<>(Arrays.asList( new TileDirections[]{TileDirections.CENTER})));
        } else {
            propertyConnectionMap.put(direction, new HashSet<>(Arrays.asList( new TileDirections[]{TileDirections.END})));
        }
    }

    public void addFeature(Feature feature, TileDirections... directions) {
        checkIfDirectionIsNotOccupied(directions);

        featureToTileDirections.put(feature, new HashSet<>(Arrays.asList(directions)));

        for (TileDirections direction: directions) {
            propertyMap.put(direction, feature);
            propertyConnectionMap.put(direction, new HashSet<>(Arrays.asList(directions)));
        }
    }

    private void checkIfDirectionIsNotOccupied(TileDirections... directions) {
        for (TileDirections direction: directions) {
            if (propertyMap.containsKey(direction))
                throw new RuntimeException("Cannot rewrite objects of feature on tile");
        }
    }

    public boolean hasCloister() {
        return propertyMap.containsKey(TileDirections.CENTER);
    }

    public boolean isComplete() {
        Set keys = propertyMap.keySet();
        switch (keys.size()) {
            case 12:
                return !keys.contains(TileDirections.CENTER);
            case 13:
                return true;
        }
        return false;
    }

    public Set<Feature> getFeatures() {
        return featureToTileDirections.keySet();
    }

    public Feature getOccupiedFeature() {
        if (isNoFollower())
            throw new RuntimeException("Trying to get feature containing follower from tile with no follower");
        return occupiedFeature;
    }

    public void returnFollower() {
        if (isNoFollower())
            throw new RuntimeException("Trying to return follower from tile that hasn't got one");
        follower.getPlayer().returnFollower();
        follower = null;
        noFollower = true;
    }

    public boolean hasCoordinates() {
        return coordinates != null;
    }

    public Set<TileDirections> getOccupiedFeatureDirections() {
        return featureToTileDirections.get(getOccupiedFeature());
    }
}
