package carcassonne.model;

import java.util.*;

/**
 * This class builds a collection of legal coordinates for current tile
 * to be placed on based on continuity of tile features.
 *
 * Created by Andrey on 19/01/16.
 */
class TilePlacingHelper {
    private HashMap<Coordinates,Tile> placedTiles;
    private Map<Coordinates, Set<Rotation>> coordinatesToRotationMap = new HashMap<>();
    private Table table;

    TilePlacingHelper(Table table) {
        this.table = table;
    }

    public Set<Coordinates> getCoordinatesSet(Tile tile) {
        Set<Coordinates> result = new HashSet<>();
        result.add(new Coordinates(0, 1));
        return result;
    }

    public void setPlacedTiles(HashMap<Coordinates,Tile> placedTiles) {
        this.placedTiles = placedTiles;
    }

    public Map getCoordinatesToRotationMap() {
        return new HashMap<>(coordinatesToRotationMap);
    }

    public void update(Tile tile) {
        /*
         * 1) tiles around existing tiles are eligible
         * 2) for each rotation check continuity of each eligible tile
         */
        Set<Coordinates> eligibleTiles = new HashSet<>();
        for (Coordinates coordinates: placedTiles.keySet()) {
            eligibleTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() - 1));
            eligibleTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() + 1));
            eligibleTiles.add(new Coordinates(coordinates.getX() + 1, coordinates.getY()));
            eligibleTiles.add(new Coordinates(coordinates.getX() - 1, coordinates.getY()));
        }

        /*
         * check rotation zero first
         */
        Rotation rotation = Rotation.DEG_0;
        for (Coordinates coordinate: eligibleTiles) {
            Set<TileDirections> directionsToCompare = new HashSet<>(Arrays.asList(
                    TileDirections.SOUTH, TileDirections.NORTH, TileDirections.EAST, TileDirections.WEST));

            /*
             * Continuity should be checked for every tile that is a neighbour
             */
            boolean continuityBroken = false;
            for (TileDirections direction: directionsToCompare) {
                Tile neighbour = table.getNeighbouringTile(coordinate.getX(), coordinate.getY(), direction);
                if (!neighbour.isNull()) {
                    if (!neighbour.isContinuous(tile, direction))
                        continuityBroken = true;
                }
            }

            if (!continuityBroken)
                Util.addSetElement(coordinatesToRotationMap, coordinate, Rotation.DEG_0);

        }
    }
}
