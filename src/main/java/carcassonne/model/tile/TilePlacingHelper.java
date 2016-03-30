package carcassonne.model.tile;

import carcassonne.model.Table;
import carcassonne.model.Util;

import java.util.*;

/**
 * This class builds a collection of legal coordinates for current tile
 * to be placed on based on continuity of tile features.
 */
public class TilePlacingHelper {
    private HashMap<Coordinates,Tile> placedTiles;
    private final Map<Coordinates, Set<Rotation>> coordinatesToRotationMap = new HashMap<>();
    private final Table table;

    public TilePlacingHelper(Table table) {
        this.table = table;
    }

    public void setPlacedTiles(HashMap<Coordinates,Tile> placedTiles) {
        this.placedTiles = placedTiles;
    }

    public Map<Coordinates, Set<Rotation>> getCoordinatesToRotationMap() {
        return new HashMap<>(coordinatesToRotationMap);
    }

    public void update(Tile tile) {
        coordinatesToRotationMap.clear();
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

        for (Rotation rotation: Rotation.values()) {
            for (Coordinates coordinate: eligibleTiles) {
                Set<TileDirection> directionsToCompare = new HashSet<>(Arrays.asList(
                        TileDirection.SOUTH, TileDirection.NORTH, TileDirection.EAST, TileDirection.WEST));

                /*
                 * Continuity should be checked for every tile that is a neighbour
                 */
                boolean continuityBroken = false;
                for (TileDirection direction: directionsToCompare) {
                    Tile neighbour = table.getNeighbouringTile(coordinate.getX(), coordinate.getY(), direction);
                    if (!neighbour.isNull()) {
                        if (!neighbour.isContinuous(tile, direction)) {
                            continuityBroken = true;
                            break;
                        }
                    }
                }

                if (!continuityBroken)
                    Util.addLinkedSetElement(coordinatesToRotationMap, coordinate, rotation);

            }
            tile = tile.turnClockwise(Rotation.DEG_90);
        }
        coordinatesToRotationMap.keySet().removeAll(placedTiles.keySet());
    }
}
