package carcassonne.model;

import java.util.HashSet;
import java.util.Set;

/**
 * RealEstate is a class that creates abstract objects from features of several tiles that are connected
 * to each other. Represents Land, Roads, Castles and Cloisters from the rules of Carcassonne.
 */
public class RealEstate {
    private HashSet<Tile> tiles = new HashSet<>();
    private final Table table;

    public RealEstate(Tile tile, Table table) {
        this.table = table;
        if (tile.isNoFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        addTile(tile);
    }

    public void addTile(Tile tile) {
        if (tile.isNull())
            throw new RuntimeException("Trying to add a NULL tile");
        if (tile.getCoordinates() == null)
            throw new RuntimeException("Tile with undefined coordinates cannot be part of real estate");
        if (! tile.isComplete())
            throw new RuntimeException("Incomplete tile cannot be part of real estate");
        if (! validCoordinates(tile))
            throw new RuntimeException("Real estate trying to add tile with duplicate or disjoint coordinates");
        if (! addedFeatureUnoccupied(tile))
            throw new RuntimeException("Trying to add occupied feature to existing real estate");

        tiles.add(tile);

        Set<Tile> adjacentTiles = new HashSet<>();
        if (! tile.isNoFollower()) {
            Set<TileDirections> occupiedFeatureDirections = tile.getOccupiedFeatureDirections();
            for (TileDirections tileDirections: occupiedFeatureDirections) {
                Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
                if (! neighbour.isNull()) {
                    adjacentTiles.add(neighbour);
                    adjacentTiles.addAll(findAdjacentTiles(neighbour, tileDirections.getNeighbour()));
                }
            }
        }
        adjacentTiles.stream().forEach(neighbour -> tiles.add(neighbour));
    }

    /*
     * Method uses recursion
     * Bad termination condition. Currently it goes no further if tile has an END destination.
     * This is bad if there is an END in the first tile
     * This problem is solved by looped method invocation
     */
    private Set<Tile> findAdjacentTiles(Tile tile, TileDirections searchDirection) {
        Set<Tile> result = new HashSet<Tile>();

        /*
         * Set of all Directions within tile that a Feature occupies
         */
        Set<TileDirections> currentTileFeatureDirections = tile.getDestinations(searchDirection);
        if(currentTileFeatureDirections.contains(TileDirections.END)) {
            result.add(tile);
            return result;
        }

        /*
         * removes TileDirections that lead back
         */
        currentTileFeatureDirections.removeAll(searchDirection.getEdge());
        for (TileDirections direction: currentTileFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(),  direction);

            if (! neighbour.isNull()) {
                result.add(neighbour);
                //TODO there is a logical error in recursive function invocation
                result.addAll(findAdjacentTiles(neighbour, direction.getNeighbour()));
            }
        }
        return result;
    }

    private boolean addedFeatureUnoccupied(Tile newTile) {
        if (!newTile.isNoFollower()) {
            for (Tile existingTile : tiles) {
                if (existingTile.isNoFollower())
                    return true;

                Set<TileDirections> existingDirections = existingTile.getOccupiedFeatureDirections();
                for (TileDirections existing : existingDirections) {
                    TileDirections shouldBeUnoccupied = existing.getNeighbour();
                    if (newTile.getOccupiedFeatureDirections().contains(shouldBeUnoccupied))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean validCoordinates(Tile tile) {
        if (tiles.size() == 0)
            return true;
        for (Tile rsTile: tiles) {
            if (rsTile.getCoordinates().equals(tile.getCoordinates()))
                return false;
        }
        return Coordinates.getAround(extractCoordinatesSet(tiles)).contains(tile.getCoordinates());
    }

    private HashSet<Coordinates> extractCoordinatesSet(HashSet<Tile> tiles) {
        HashSet<Coordinates> result = new HashSet<>();
        for (Tile tile: tiles) result.add(tile.getCoordinates());
        return result;
    }

    public Set<Tile> getTileSet() {
        return tiles;
    }
}
