package carcassonne.model;

import java.util.HashMap;
import java.util.Set;

public class Tile {
    private Coordinates coordinates;
    private boolean noFollowers = true;
    private HashMap<TileDirections, Property> propertyMap = new HashMap<>();
    /*
     * A property can "connect" one TileDirections with the other. For example, a road from EAST to WEST
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
        // TODO Auto-generated method stub
        return coordinates;
    }

    public boolean isNoFollowers() {
        return noFollowers;
    }

    public void placeFollower() {
        noFollowers = false;
    }

    public TileDirections[] getDestinations(TileDirections dir) {
        return propertyConnectionMap.get(dir);
    }

    public void addProperty(Property property, TileDirections mandatoryDirection, TileDirections... directions) {
        TileDirections[] completeDirections = new TileDirections[directions.length + 1];
        completeDirections[0] = mandatoryDirection;
        System.arraycopy(directions, 0, completeDirections, 1, directions.length);

        for (TileDirections direction: completeDirections) {
            if (propertyMap.containsKey(direction))
                throw new RuntimeException("Cannot rewrite objects of property on tile");
        }

        if (completeDirections.length == 1) {
            propertyMap.put(completeDirections[0], property);
            if (completeDirections[0] == TileDirections.CENTER)
                propertyConnectionMap.put(completeDirections[0], new TileDirections[] {TileDirections.CENTER});
            else
                propertyConnectionMap.put(completeDirections[0], new TileDirections[] {TileDirections.END});
            return;
        }
        for (TileDirections direction: completeDirections) {
            propertyMap.put(direction, property);
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
}
