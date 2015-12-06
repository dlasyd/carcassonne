package carcassonne.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrey on 04/12/15.
 */
public class RealEstate {
    private HashSet<Tile> tiles = new HashSet<>();
    private Table table;

    public RealEstate(Tile tile) {
        if (tile.isNoFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        addTile(tile);
    }

    /*
     * TODO: remove no args constructor
     */
    public RealEstate() {}
    public RealEstate(Tile tile, Table table) {
        this.table = table;
        if (tile.isNoFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        addTile(tile);
    }

    public void addTile(Tile tile) {
        /*
         * TODO refactor this
         */
        Set<Tile> adjacentTiles = new HashSet<>();
        if (! tile.isNoFollower()) {
            Set<TileDirections> featureDirections = tile.getOccupiedFeatureDirections();
            if (table != null) {
                System.out.println("feature directions" + featureDirections);
                featureDirections.stream().forEach(tileDirections -> {
                    Tile t = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
                    if (! t.isNull())
                        adjacentTiles.add(t);
                });
            }
        }

        if (tile.getCoordinates() == null)
            throw new RuntimeException("Tile with undefined coordinates cannot be part of real estate");
        if (! tile.isComplete())
            throw new RuntimeException("Incomplete tile cannot be part of real estate");
        if (! validCoordinates(tile))
            throw new RuntimeException("Real estate trying to add tile with duplicate or disjoint coordinates");
        if (! addedFeatureUnoccupied(tile))
            throw new RuntimeException("Trying to add occupied feature to existing real estate");

        tiles.add(tile);

        adjacentTiles.stream().forEach(neighbour -> tiles.add(neighbour));

    }

    private boolean addedFeatureUnoccupied(Tile newTile) {
        if (!newTile.isNoFollower()) {
            for (Tile existingTile : tiles) {
                if (existingTile.isNoFollower())
                    return true;

                Set<TileDirections> existingDirections = existingTile.getOccupiedFeatureDirections();
                for (TileDirections existing : existingDirections) {
                    TileDirections shouldBeUnoccupied = existing.getNeighbour();
                    Set<TileDirections> occupiedFeature = newTile.getOccupiedFeatureDirections();
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

    public Set<Tile> getTileSet() {
        return tiles;
    }
}
