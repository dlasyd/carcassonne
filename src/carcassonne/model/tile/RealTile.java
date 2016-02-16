package carcassonne.model.tile;

import carcassonne.model.Player;
import carcassonne.model.feature.Feature;

import java.util.*;

/**
 * RealTile represents a tile from a game. There are 3 instance variable Collections that represent
 * connection between features of the tile and tileDirections. They are based on the same data and changed together.
 * This redundancy exists to make retrieving data easier.
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

    RealTile(RealTile realTile) {
        this.tileName = realTile.tileName;
        this.coordinates = realTile.coordinates;
        this.currentRotation = realTile.currentRotation;
        this.occupiedFeature = realTile.occupiedFeature;
        this.follower = realTile.follower;
        this.noFollower = realTile.noFollower;
        this.featureToTileDirections = new HashMap<>(realTile.featureToTileDirections);
        this.propertyMap = new HashMap<>(realTile.propertyMap);
        this.propertyConnectionMap = new HashMap<>(realTile.propertyConnectionMap);
    }

    RealTile(TileName tileName) {
        this.tileName = tileName;
    }

    RealTile(int x, int y) {
        coordinates = new Coordinates (x, y);
    }

    @Override
    public Tile turnRight(Rotation angle) {
        RealTile newTile = new RealTile(this);
        newTile.rotateValueSet(newTile.featureToTileDirections, angle);
        newTile.rotateKeys(newTile.propertyConnectionMap, angle);
        newTile.rotateValueSet(newTile.propertyConnectionMap, angle);
        newTile.rotateKeys(newTile.propertyMap, angle);
        newTile.currentRotation = Rotation.values()[((currentRotation.ordinal() + angle.ordinal()) % 4)];
        return newTile;
    }
    
    @Override
    public Tile copyFeatures(Tile referenceTile) {
        RealTile newTile = new RealTile(this);
        newTile.featureToTileDirections = ((RealTile) referenceTile).featureToTileDirections;
        newTile.propertyMap = ((RealTile) referenceTile).propertyMap;
        newTile.propertyConnectionMap = ((RealTile) referenceTile).propertyConnectionMap;
        return newTile;
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

    @Override
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

    @Override
    public Tile setCoordinates(int x, int y) {
        RealTile newTile = new RealTile(this);
        newTile.coordinates = new Coordinates (x, y);
        return newTile;
    }

    @Override
    public Tile placeFollower(Player player, Feature feature) {
        RealTile newTile = new RealTile(this);
        newTile.noFollower = false;
        newTile.occupiedFeature = feature;
        newTile.follower = new Follower(player);
        player.placeFollower();
        return newTile;
    }

    @Override
    public Tile placeFollower(Player player, TileDirections direction) {
        if (propertyMap.get(direction) == null)
            throw new RuntimeException("Cannot place follower using tileDirection because there is no corresponding feature");
        return placeFollower(player, propertyMap.get(direction));
    }

    @Override
    public void addFeature(Feature feature, TileDirections direction) {
        addFeature(feature, new TileDirections[]{direction});
    }

    @Override
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

    @Override
    public boolean hasCloister() {
        return propertyMap.containsKey(TileDirections.CENTER);
    }

    @Override
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

    @Override
    public Tile returnFollowerToPlayer() {
        if (!hasFollower())
            throw new RuntimeException("Trying to return follower from tile that hasn't got one");
        RealTile newTile = new RealTile(this);
        newTile.follower.getPlayer().returnFollower();
        newTile.follower = null;
        newTile.noFollower = true;
        return newTile;
    }

    //<editor-fold desc="Getters">
    @Override
    public boolean hasCoordinates() {
        return coordinates != null;
    }

    @Override
    public boolean isNull() {
        return false;
    }


    @Override
    public boolean hasFollower() {
        return !noFollower;
    }

    @Override
    public Player getFollowerOwner() {
        assert (follower != null);
        return follower.getPlayer();
    }

    @Override
    public TileName getName() {
        return tileName;
    }

    @Override
    public Feature getFeature(TileDirections direction) {
        return Feature.createFeature(propertyMap.get(direction));
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

    @Override
    public Set<TileDirections> getOccupiedFeatureDirections() {
        return new HashSet<>(featureToTileDirections.get(getOccupiedFeature()));
    }

    @Override
    public LinkedHashSet<TileDirections> getFeatureTileDirections(Feature feature) {
        return new LinkedHashSet<>(featureToTileDirections.get(feature));
    }

    @Override
    public Set<Feature> getFeatures() {
        return new HashSet<>(featureToTileDirections.keySet());
    }

    @Override
    public Feature getOccupiedFeature() {
        if (!hasFollower())
            throw new RuntimeException("Trying to get feature containing follower from tile with no follower");
        return occupiedFeature;
    }

    @Override
    public Set<TileDirections> getDestinations(TileDirections dir) {
        return new HashSet<>(propertyConnectionMap.get(dir));
    }

    @Override
    public Coordinates getCoordinates() {
        if (coordinates == null)
            throw new RuntimeException("Tile has no coordinates");
        return coordinates;
    }

    @Override
    public int getX() {
        return coordinates.getX();
    }

    @Override
    public int getY() {
        return coordinates.getY();
    }

    /*
     * This method is used in placeTile(...) method of Table
     *
     * It does not matter which of occupied feature TileDirections is returned, they
     * are logically equivalent
     */
    @Override
    public TileDirections getFollowerTileDirection() {
        return featureToTileDirections.get(occupiedFeature).iterator().next();
    }
    //</editor-fold>

    /**
     * This method is invoked by Land getPoints() method.
     * It is used to know weather or not a city that is a feature of one of the land tiles "touches" that land
     *
     * @param feature a feature of a tile that you are checking
     * @param expectedTileDirections of a different feature
     * @return true if feature and tile directions are next to each other at least at one expectedTileDirection
     */
    @Override
    public boolean featureBordersWith(Feature feature, Set<TileDirections> expectedTileDirections) {
        Set<TileDirections> adjacentTileDirections = new HashSet<>();
        Set<TileDirections> currentFeatureTileDirections = featureToTileDirections.get(feature);
        for (TileDirections direction: currentFeatureTileDirections) {
            adjacentTileDirections.addAll(direction.getAdjacentDirections());
        }
        adjacentTileDirections.removeAll(currentFeatureTileDirections);
        adjacentTileDirections.retainAll(expectedTileDirections);

        return adjacentTileDirections.size() > 0;
    }

    @Override
    public boolean featureEqual(Tile tile) {
        RealTile otherTile = (RealTile) tile;

        /*
         * featureToTileDirections and propertyConnectionMap are not checked because they
         * change together with propertyMap and contain the same information packed differently
         */
        for (TileDirections direction: propertyMap.keySet()) {
            if (!this.propertyMap.get(direction).isSameType(otherTile.propertyMap.get(direction)))
                return false;
        }
        return true;
    }

    @Override
    public boolean directionsEqual(Tile referenceTile) {
        return this.propertyConnectionMap.equals(((RealTile) referenceTile).propertyConnectionMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealTile))
            return false;

        RealTile that = (RealTile) obj;

        if (this.tileName != that.tileName || this.currentRotation != that.currentRotation)
            return false;

        if ((this.coordinates == null) ? that.coordinates != null : !this.coordinates.equals(that.coordinates))
            return false;

        /*
         * Because all feature instances are equal only to themselves, when comparing collections that contain them
         * method isSameType() should be used
         */

        for (TileDirections direction: propertyMap.keySet()) {
            if (!this.propertyMap.get(direction).isSameType(that.propertyMap.get(direction)))
                return false;
        }

        /*
         * featureToTileDirections and propertyConnectionMap are not checked because they
         * change together with propertyMap and contain the same information packed differently
         */

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        if (hasCoordinates())
            return "T(" + getX() + "," + getY() + ")";
        else return "Not placed yet";
    }
}
