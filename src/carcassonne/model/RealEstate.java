package carcassonne.model;

import java.util.*;

/**
 * RealEstate is a class that creates abstract objects from features of several tiles that are connected
 * to each other. Represents Land, Roads, Castles and Cloisters from the rules of Carcassonne.
 */
public class RealEstate {
    private final Map<Tile, Set<TileDirections>> tilesAndFeatureTileDirections = new HashMap<>();
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

        if (tilesAndFeatureTileDirections.isEmpty()) {
            tilesAndFeatureTileDirections.put(tile, tile.getOccupiedFeatureDirections());
        } else {
            Set<Tile> neighbourRealEstateTiles = getNeighbourRealEstateTiles(tile);

            for (Tile neighbour: neighbourRealEstateTiles) {
                Set<TileDirections> featureDirections = getNeighbourFeaturesContinuation(neighbour, tile);
                if (!featureDirections.isEmpty()) {
                    tilesAndFeatureTileDirections.put(tile, featureDirections);
                    break;
                }
            }

            throw new RuntimeException("Trying to add inappropriately place tile to real estate");
        }

        addAdjacentTiles(tile);

    }

    private Set<Tile> getNeighbourRealEstateTiles(Tile tile) {
        Set<Tile> result = new HashSet<>();
        int[][] aroundCoordinates = {{tile.getX(), tile.getY() - 1}, {tile.getX(), tile.getY() + 1},
                {tile.getX() - 1, tile.getY()}, {tile.getX() + 1, tile.getY()}};
        for (int i = 0; i < 4; i++) {
            Tile neighbour = table.getTile(aroundCoordinates[i][0], aroundCoordinates[i][1]);
            if (!neighbour.isNull() && tilesAndFeatureTileDirections.containsKey(neighbour))
                result.add(neighbour);
        }
        return result;
    }

    private void addAdjacentTiles(Tile tile) {
        Map<Tile, Set<TileDirections>> adjacentTiles = new HashMap<>();
        Set<TileDirections> occupiedFeatureDirections = tilesAndFeatureTileDirections.get(tile);
        assert(occupiedFeatureDirections != null);

        for (TileDirections tileDirections : occupiedFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
            if (!neighbour.isNull() && !tilesAndFeatureTileDirections.containsKey(neighbour)) {
                adjacentTiles.put(neighbour, neighbour.getDestinations(tileDirections));
                adjacentTiles.putAll(findAdjacentTiles(neighbour, tileDirections.getNeighbour(), tile));
            }
        }

        tilesAndFeatureTileDirections.putAll(adjacentTiles);
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
            result.put(tile, new HashSet<>(Collections.singletonList(TileDirections.END)));
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
        Set<Tile> tiles = new HashSet<>(tilesAndFeatureTileDirections.keySet());
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
        Set<Tile> tiles = new HashSet<>(tilesAndFeatureTileDirections.keySet());
        if (tiles.size() == 0)
            return true;
        for (Tile rsTile: tiles) {
            if (rsTile.getCoordinates().equals(tile.getCoordinates()))
                return false;
        }
        return Coordinates.getAround(extractCoordinatesSet(tiles)).contains(tile.getCoordinates());
    }

    private Set<Coordinates> extractCoordinatesSet(Set<Tile> tiles) {
        Set<Coordinates> result = new HashSet<>();
        for (Tile tile: tiles) result.add(tile.getCoordinates());
        return result;
    }

    public Set<Tile> getTileSet() {
        return new HashSet<>(tilesAndFeatureTileDirections.keySet());
    }

    void update(Tile tile) {
        addIfCanBeConnectedToRealEstate(tile);
    }

    private void addIfCanBeConnectedToRealEstate(Tile tile) {
        int[][] aroundCoordinates = {{tile.getX(), tile.getY() - 1}, {tile.getX(), tile.getY() + 1},
                {tile.getX() - 1, tile.getY()}, {tile.getX() + 1, tile.getY()}};
        TileDirections[] neighbourDirection = {TileDirections.NORTH, TileDirections.SOUTH, TileDirections.WEST,
        TileDirections.EAST};

        for (int i = 0; i < 4; i++) {
            Tile t = table.getTile(aroundCoordinates[i][0], aroundCoordinates[i][1]);
            if (! t.isNull() && tilesAndFeatureTileDirections.containsKey(t)) {
                Set<TileDirections> directions = tilesAndFeatureTileDirections.get(t);
                Set<TileDirections> targetEdge = neighbourDirection[i].getNeighbour().getEdge();
                for (TileDirections edge: targetEdge) {
                    if (directions.contains(edge)) {
                        tilesAndFeatureTileDirections.put(tile, tile.getDestinations(edge.getNeighbour()));
                        addAdjacentTiles(tile);
                    }
                }
            }
        }
    }

    private Set<TileDirections> getNeighbourFeaturesContinuation(Tile neighbour, Tile recipient) {
        Set<TileDirections> result = new HashSet<>();
        TileDirections recipientBorder;
        if (neighbour.getX() == recipient.getX()) {
            if (neighbour.getY() == recipient.getY() + 1) {
                recipientBorder = TileDirections.SOUTH;
            } else if (neighbour.getY() == recipient.getY() - 1) {
                recipientBorder = TileDirections.NORTH;
            } else {
                throw new RuntimeException("Trying to get continuation of feature from illegally placed tiles");
            }
        } else if (neighbour.getY() == recipient.getY()) {
            if (neighbour.getX() == recipient.getX() + 1) {
                recipientBorder = TileDirections.EAST;
            } else if (neighbour.getX() == recipient.getX() - 1) {
                recipientBorder = TileDirections.WEST;
            } else {
                throw new RuntimeException("Trying to get continuation of feature from illegally placed tiles");
            }

        } else {
            throw new RuntimeException("Trying to get continuation of feature from illegally placed tiles");
        }

        Set<TileDirections> neighbourFeatureDirections = tilesAndFeatureTileDirections.get(neighbour);
        Set<TileDirections> edge = recipientBorder.getNeighbour().getEdge();

        neighbourFeatureDirections.retainAll(edge);
        /*
         * neighbourFeatureDirections now contains TileDirections of Feature that continues onto recipient tile
         * on the border with recipient tile
         */

        for (TileDirections direction: neighbourFeatureDirections) {
            direction = direction.getNeighbour();
            return recipient.getDestinations(direction);
        }

        return result;
    }
}
