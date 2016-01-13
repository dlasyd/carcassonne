package carcassonne.model;

import java.util.*;

import static carcassonne.model.TileDirections.*;

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

    public void turnRight(Rotation angle) {
        rotateValueSet(featureToTileDirections, angle);
        rotateKeys(propertyConnectionMap, angle);
        rotateValueSet(propertyConnectionMap, angle);
        rotateKeys(propertyMap, angle);

    }

    @Override
    public boolean featureEqual(Tile tile) {
        RealTile otherTile = (RealTile) tile;
        if (this.featureToTileDirections.equals(otherTile.featureToTileDirections) &
                this.propertyMap.equals(otherTile.propertyMap) &
                this.propertyConnectionMap.equals(otherTile.propertyConnectionMap))
            return true;
        return false;
    }

    @Override
    public boolean directionsEqual(Tile referenceTile) {
        if (this.propertyConnectionMap.equals( ((RealTile)referenceTile).propertyConnectionMap) )
            return true;
        else
            return false;
    }

    @Override
    public void copyFeatures(Tile referenceTile) {
        featureToTileDirections = ((RealTile) referenceTile).featureToTileDirections;
        propertyMap = ((RealTile) referenceTile).propertyMap;
        propertyConnectionMap = ((RealTile) referenceTile).propertyConnectionMap;
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

    /*
     * should place follower only if tile is not part of real estate
     */
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
        Set keys = new HashSet(propertyMap.keySet());
        switch (keys.size()) {
            case 12:
                return !keys.contains(TileDirections.CENTER);
            case 13:
                return true;
        }
        return false;
    }

    public Set<Feature> getFeatures() {
        return new HashSet<Feature>(featureToTileDirections.keySet());
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

    @Override
    /*
     * returns unoccupied directions. CENTER is excluded from return because they are not needed in
     * the context of use of the method (the method is created to be used in RealEstateTest)
     */
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
