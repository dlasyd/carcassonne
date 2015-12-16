package carcassonne.model;

import java.util.*;

/**
 * RealEstate is a class that creates abstract objects from features of several tiles that are connected
 * to each other. Represents Land, Roads, Castles and Cloisters from the rules of Carcassonne.
 */
public class RealEstate {
    private HashSet<Tile> tiles = new HashSet<>();
    private Map<Tile, Set<TileDirections>> tileDirections = new HashMap<Tile, Set<TileDirections>>();
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
        // TODO this is not acceptable. tileDirections should have elements for all tiles
        if (!tile.isNoFollower())
            tileDirections.put(tile, tile.getOccupiedFeatureDirections());

        addAdjacentTiles(tile);

    }

    private void addAdjacentTiles(Tile tile) {
        Map<Tile, Set<TileDirections>> adjacentTiles = new HashMap<>();
        if (! tile.isNoFollower()) {
            Set<TileDirections> occupiedFeatureDirections = tile.getOccupiedFeatureDirections();
            for (TileDirections tileDirections: occupiedFeatureDirections) {
                Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
                if (! neighbour.isNull()) {
                    adjacentTiles.put(neighbour,neighbour.getDestinations(tileDirections));
                    adjacentTiles.putAll(findAdjacentTiles(neighbour, tileDirections.getNeighbour(), tile));
                }
            }
        }
        Set<Tile> adjacentTilesSet = adjacentTiles.keySet();
        for (Tile adjacentTile: adjacentTilesSet) {
            tiles.add(adjacentTile);
        }
        tileDirections.putAll(adjacentTiles);
    }

    /*
     * Method uses recursion
     *
     * loopBreakingTile is a tile that is used to create RealEstate(in constructor)
     */
    private Map<Tile, Set<TileDirections>> findAdjacentTiles(Tile tile, TileDirections directionWithFeature, Tile loopBreakingTile) {
        Map<Tile, Set<TileDirections>> result = new HashMap<>();

        if (tile == loopBreakingTile) {
            return result;
        }

        /*
         * Set of all Directions within tile that a Feature occupies
         */
        Set<TileDirections> currentTileFeatureDirections = tile.getDestinations(directionWithFeature);
        if(currentTileFeatureDirections.contains(TileDirections.END)) {
            result.put(tile, new HashSet<>(Arrays.asList(TileDirections.END)));
            return result;
        }

        /*
         * removes TileDirections that lead back
         */
        currentTileFeatureDirections.removeAll(directionWithFeature.getEdge());
        for (TileDirections direction: currentTileFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(),  direction);

            if (! neighbour.isNull()) {
                result.put(neighbour, neighbour.getDestinations(direction));
                result.putAll(findAdjacentTiles(neighbour, direction.getNeighbour(), loopBreakingTile));
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

    void update(Tile tile) {
        addIfCanBeConnectedToRealEstate(tile);
    }

    private boolean addIfCanBeConnectedToRealEstate(Tile tile) {
        Set<Tile> tilesAround = new HashSet<>();
        int[][] aroundCoordinates = {{tile.getX(), tile.getY() - 1}, {tile.getX(), tile.getY() + 1},
                {tile.getX() - 1, tile.getY()}, {tile.getX() + 1, tile.getY()}};
        TileDirections[] neighbourDirection = {TileDirections.NORTH, TileDirections.SOUTH, TileDirections.WEST,
        TileDirections.EAST};

        for (int i = 0; i < 4; i++) {
            Tile t = table.getTile(aroundCoordinates[i][0], aroundCoordinates[i][1]);
            if (! t.isNull() && tileDirections.containsKey(t)) {
                Set<TileDirections> directions = tileDirections.get(t);
                Set<TileDirections> targetEdge = neighbourDirection[i].getNeighbour().getEdge();
                for (TileDirections edge: targetEdge) {
                    if (directions.contains(edge)) {
                        addTile(tile);
                        tileDirections.put(tile, tile.getDestinations(edge.getNeighbour()));
                        addAdjacentTiles(tile);
                        return true;
                    }
                }
            }
        }

        if (tilesAround.isEmpty())
            return false;

        return false;
    }
}
