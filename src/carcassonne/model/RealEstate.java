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
        if (tile.getCoordinates() == null)
            throw new RuntimeException("Tile with undefined coordinates cannot be part of real estate");
        if (! tile.isComplete())
            throw new RuntimeException("Incomplete tile cannot be part of real estate");
        if (! validCoordinates(tile))
            throw new RuntimeException("Real estate trying to add tile with duplicate or disjoint coordinates");
        if (! addedFeatureUnoccupied(tile))
            throw new RuntimeException("Trying to add occupied feature to existing real estate");

        tiles.add(tile);

        /*
         * decide if you need to go to one more tile
         *
         */
        Set<Tile> adjacentTiles = new HashSet<>();
        if (! tile.isNoFollower()) {
            Set<TileDirections> featureDirections = tile.getOccupiedFeatureDirections();
            featureDirections.stream().forEach(tileDirections -> {
                Tile t = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
                if (! t.isNull()) {
                    adjacentTiles.add(t);
                    Set<TileDirections> spanningFeatureDirections = t.getDestinations(tileDirections.getNeighbour());
                    if ( !spanningFeatureDirections.contains(TileDirections.END)) {
                        /*
                         * 1) remove TileDirection that leads to previous tile
                         * 2) add current tile to adjacent tiles
                         * 3) repeat operation for the next tile
                         */
                        assert spanningFeatureDirections.remove(tileDirections.getNeighbour());
                        spanningFeatureDirections.stream().forEach(direction -> {
                            Tile t2 = table.getNeighbouringTile(t.getX(), t.getY(), tileDirections);
                            if (! t2.isNull())
                                adjacentTiles.add(t2);
                        });
                    }
                }
            });

            /*
             * Second wave of adding tiles
             * Which features of tile are part of real estate?
             * 1) select features of real estate
             * 2) same cycle as above for each tile
             */


        }
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
