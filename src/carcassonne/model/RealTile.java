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
    private TileName tileName;
    private Coordinates coordinates;
    private Rotation currentRotation = Rotation.DEG_0;
    private Feature occupiedFeature = null;
    private Follower follower;
    private boolean noFollower = true;
    private HashMap<Feature,Set<TileDirections>> featureToTileDirections = new HashMap<>();
    private HashMap<TileDirections, Feature> propertyMap = new HashMap<>();

    /*
     * A feature can "connect" one TileDirections with the other. For example, a road from EAST to WEST
     */
    private HashMap<TileDirections, Set<TileDirections>> propertyConnectionMap = new HashMap<>();

    RealTile() {}

    RealTile(TileName tileName) {
        this.tileName = tileName;
    }

    RealTile(int x, int y) {
        setCoordinates(x, y);
    }

    public void turnRight(Rotation angle) {
        rotateValueSet(featureToTileDirections, angle);
        rotateKeys(propertyConnectionMap, angle);
        rotateValueSet(propertyConnectionMap, angle);
        rotateKeys(propertyMap, angle);
        int numberOf90Rotations = currentRotation.getNumberOf90Rotations() + angle.getNumberOf90Rotations();
        currentRotation = Rotation.getValue(numberOf90Rotations % 4);
    }

    @Override
    public boolean featureEqual(Tile tile) {
        RealTile otherTile = (RealTile) tile;
        return this.featureToTileDirections.equals(otherTile.featureToTileDirections) &
                this.propertyMap.equals(otherTile.propertyMap) &
                this.propertyConnectionMap.equals(otherTile.propertyConnectionMap);
    }

    @Override
    public boolean directionsEqual(Tile referenceTile) {
        return this.propertyConnectionMap.equals(((RealTile) referenceTile).propertyConnectionMap);
    }

    @Override
    public void copyFeatures(Tile referenceTile) {
        featureToTileDirections = ((RealTile) referenceTile).featureToTileDirections;
        propertyMap = ((RealTile) referenceTile).propertyMap;
        propertyConnectionMap = ((RealTile) referenceTile).propertyConnectionMap;
    }

    @Override
    public TileName getName() {
        return tileName;
    }

    @Override
    public boolean isContinuous(Tile tile, TileDirections direction) {
        Set<TileDirections> directionsToCheck = direction.getEdge();
        for (TileDirections edgeDirection: directionsToCheck) {
            if (!tile.getFeature(edgeDirection).isSameType(this.getFeature(edgeDirection.getNeighbour())))
                return false;
        }
        return true;
    }

    public Rotation getCurrentRotation() {
        return currentRotation;
    }

    private <T> void rotateKeys(HashMap<TileDirections, T> map, Rotation angle) {
        Set<TileDirections> keySet = new HashSet<>(map.keySet());
        HashMap<TileDirections, T> resultMap = new HashMap<>();

        for (TileDirections direction: keySet) {
            resultMap.put(direction.turnRight(angle), map.get(direction));
        }
        map.keySet().removeAll(map.keySet());
        map.putAll(resultMap);
    }

    private <T> void rotateValueSet(HashMap<T, Set<TileDirections>> map, Rotation angle) {
        Set<T> keySet = new HashSet<>(map.keySet());
        for (T type: keySet) {
            Set<TileDirections> directionsToRotate = map.get(type);
            map.remove(type);
            Set<TileDirections> rotatedDirections = new HashSet<>();
            for (TileDirections direction: directionsToRotate) {
                rotatedDirections.add(direction.turnRight(angle));
            }
            map.put(type, rotatedDirections);
        }
    }

    void setCoordinates(int x, int y) {
        coordinates = new Coordinates (x, y);
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
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
        return new HashSet<>(propertyConnectionMap.get(dir));
    }

    public void addFeature(Feature feature, TileDirections direction) {
        addFeature(feature, new TileDirections[]{direction});
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
        Set keys = new HashSet<>(propertyMap.keySet());
        switch (keys.size()) {
            case 12:
                return !keys.contains(TileDirections.CENTER);
            case 13:
                return true;
        }
        return false;
    }

    public Set<Feature> getFeatures() {
        return new HashSet<>(featureToTileDirections.keySet());
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

    @Override
    Player getFollowerOwner() {
        assert (follower != null);
        return follower.getPlayer();
    }

    @Override
    Feature getFeature(TileDirections direction) {
        return propertyMap.get(direction);
    }

    /*
     * returns unoccupied directions. CENTER is excluded from return because they are not needed in
     * the context of use of the method (the method is created to be used in RealEstateTest)
     */
    @Override
    public TileDirections[] getUnoccupiedDirections() {
        Set<TileDirections> result = new HashSet<>();
        result.addAll(Arrays.asList(TileDirections.values()));
        result.remove(TileDirections.CENTER);
        result.removeAll(propertyMap.keySet());
        return result.toArray(new TileDirections[result.size()]);
    }

    public boolean hasCoordinates() {
        return coordinates != null;
    }

    public Set<TileDirections> getOccupiedFeatureDirections() {
        return new HashSet<>(featureToTileDirections.get(getOccupiedFeature()));
    }

    public String toString() {
        if (hasCoordinates())
            return "T(" + getX() + "," + getY() + ")";
        else return "Not placed yet";
    }
}
