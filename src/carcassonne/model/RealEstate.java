package carcassonne.model;

import java.util.HashSet;

/**
 * Created by Andrey on 04/12/15.
 */
public class RealEstate {
    private HashSet<Tile> tiles = new HashSet<>();

    public void addTile(Tile tile) {
        if (tile.getCoordinates() == null)
            throw new RuntimeException("Tile with undefined coordinates cannot be part of real estate");
        if (! tile.isComplete())
            throw new RuntimeException("Incomplete tile cannot be part of real estate");
        if (! validCoordinates(tile))
            throw new RuntimeException("Real estate trying to add tile with duplicate coordinates");

        tiles.add(tile);

    }

    private boolean validCoordinates(Tile tile) {
        for (Tile rsTile: tiles) {
            if (rsTile.getCoordinates().equals(tile.getCoordinates()))
                return false;
        }
        if (! Coordinates.getAround(extractCoordinatesSet(tiles)).contains(tile.getCoordinates()))
            return false;
        return true;
    }

    private HashSet<Coordinates> extractCoordinatesSet(HashSet<Tile> tiles) {
        HashSet<Coordinates> result = new HashSet<>();
        for (Tile tile: tiles) {
            result.add(tile.getCoordinates());
        }
        return result;
    }
}
