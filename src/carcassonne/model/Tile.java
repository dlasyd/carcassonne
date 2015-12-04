package carcassonne.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tile {
    private Coordinates coordinates;
    private boolean noFollower = true;
    private Feature occupiedFeature = null;
    private Follower follower;
    private HashSet<Feature> features = new HashSet<>();
    private HashMap<TileDirections, Feature> propertyMap = new HashMap<>();

    /*
     * A feature can "connect" one TileDirections with the other. For example, a road from EAST to WEST
     */
    private HashMap<TileDirections, TileDirections[]> propertyConnectionMap = new HashMap<>();

    Tile() {}

    Tile(int x, int y) {
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

    Coordinates getCoordinates() {
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

    public TileDirections[] getDestinations(TileDirections dir) {
        return propertyConnectionMap.get(dir);
    }

    public void addProperty(Feature feature, TileDirections mandatoryDirection, TileDirections... directions) {
        TileDirections[] completeDirections = new TileDirections[directions.length + 1];
        completeDirections[0] = mandatoryDirection;
        System.arraycopy(directions, 0, completeDirections, 1, directions.length);

        features.add(feature);

        for (TileDirections direction: completeDirections) {
            if (propertyMap.containsKey(direction))
                throw new RuntimeException("Cannot rewrite objects of feature on tile");
        }

        if (completeDirections.length == 1) {
            propertyMap.put(completeDirections[0], feature);
            if (completeDirections[0] == TileDirections.CENTER)
                propertyConnectionMap.put(completeDirections[0], new TileDirections[] {TileDirections.CENTER});
            else
                propertyConnectionMap.put(completeDirections[0], new TileDirections[] {TileDirections.END});
            return;
        }
        for (TileDirections direction: completeDirections) {
            propertyMap.put(direction, feature);
            propertyConnectionMap.put(direction, completeDirections);
        }


    }

    public boolean hasCloister() {
        if (propertyMap.containsKey(TileDirections.CENTER))
            return true;
        else
            return false;
    }

    public boolean isComplete() {
        Set keys = propertyMap.keySet();
        switch (keys.size()) {
            case 12:
                if (keys.contains(TileDirections.CENTER))
                    return false;
                else
                    return true;
            case 13:
                return true;
        }
        return false;
    }

    public HashSet<Feature> getFeatures() {
        return features;
    }

    public Feature getOccupiedFeature() {
        if (noFollower == true)
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
}
