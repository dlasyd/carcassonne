package carcassonne.model.realestate;

import carcassonne.model.*;
import carcassonne.model.Feature.CloisterPiece;
import carcassonne.model.Feature.LandPiece;
import carcassonne.model.Feature.RoadPiece;
import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RealEstate is a class that creates abstract objects from features of several tiles that are connected
 * to each other. Represents Land, Roads, Castles and Cloisters from the rules of Carcassonne.
 */
public abstract class RealEstate {
    private static Table table;
    final Map<Tile, Set<TileDirections>> tilesAndFeatureTileDirections = new HashMap<>();
    private final ImmutableRealEstate immutableRealEstate;
    boolean finished = false;
    private Tile firstTile;

    RealEstate(Tile tile) {
        immutableRealEstate = new ImmutableRealEstate(this, tile);
        if (!tile.hasFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        addTile(tile);
        firstTile = tile;
    }

    static RealEstate getCopy(RealEstate realEstate) {
        RealEstate newRealEstate = RealEstate.getInstance(realEstate.firstTile);
        Util.addAllSetElements(newRealEstate.tilesAndFeatureTileDirections,
                realEstate.tilesAndFeatureTileDirections);
        return newRealEstate;
    }

    static RealEstate getInstance(Tile tile) {
        if (tile.getOccupiedFeature() instanceof RoadPiece)
            return new Road(tile);
        if (tile.getOccupiedFeature() instanceof CloisterPiece)
            return new Cloister(tile);
        if (tile.getOccupiedFeature() instanceof LandPiece)
            return new Land(tile);

        return new Castle(tile);
    }

    Table getTable() {
        return table;
    }

    static void setTable(Table table) {
        RealEstate.table = table;
    }

    abstract boolean isFinished();

    public void addTile(Tile tile) {
        checkArguments(tile);
        addTileAndConnectedTiles(tile);
    }

    void update(Tile tile) {
        addIfCanBeConnectedToRealEstate(tile);
    }

    private void checkArguments(Tile tile) {
        if (tile.isNull())
            throw new RuntimeException("Trying to add a NULL tile");
        if (tile.getCoordinates() == null)
            throw new RuntimeException("Tile with undefined coordinates cannot be part of real estate");
        if (!tile.isComplete()) {
            throw new RuntimeException("Incomplete tile cannot be part of real estate");
        }
        if (!validCoordinates(tile))
            throw new RuntimeException("Real estate trying to add tile with duplicate or disjoint coordinates");
        if (!addedFeatureUnoccupied(tile))
            throw new RuntimeException("Trying to add occupied Feature to existing real estate");
    }

    /*
     * Correct for City, Road and Land. Cloister class overrides this method
     */
    void addTileAndConnectedTiles(Tile tile) {
        tilesAndFeatureTileDirections.put(tile, tile.getOccupiedFeatureDirections());
        addAdjacentTiles(tile);
    }

    private void addAdjacentTiles(Tile tile) {
        Map<Tile, Set<TileDirections>> adjacentTiles = new HashMap<>();
        Set<TileDirections> occupiedFeatureDirections = tilesAndFeatureTileDirections.get(tile);
        assert (occupiedFeatureDirections != null);

        for (TileDirections tileDirections : occupiedFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirections);
            if (!neighbour.isNull()) {
                Util.addLinkedSetElement(adjacentTiles, neighbour, neighbour.getDestinations(tileDirections.getNeighbour()));
                Util.addAllSetElements(adjacentTiles, findAdjacentTiles(neighbour, tileDirections.getNeighbour(), new HashSet<>()));
            }
        }

        Util.addAllSetElements(tilesAndFeatureTileDirections, adjacentTiles);
    }

    /*
     * Method uses recursion
     */
    private Map<Tile, Set<TileDirections>> findAdjacentTiles(Tile startTile, TileDirections directionWithFeature, Set<Tile> visitedTiles) {
        Map<Tile, Set<TileDirections>> result = new HashMap<>();

        Set<TileDirections> currentTileFeatureDirections = startTile.getDestinations(directionWithFeature);
        currentTileFeatureDirections.removeAll(directionWithFeature.getEdge());
        for (TileDirections direction : currentTileFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(startTile.getX(), startTile.getY(), direction);

            if (!neighbour.isNull() && !visitedTiles.contains(neighbour)) {
                visitedTiles.add(neighbour);
                result.put(neighbour, neighbour.getDestinations(direction.getNeighbour()));
                result.putAll(findAdjacentTiles(neighbour, direction.getNeighbour(), visitedTiles));
            }
        }
        return result;
    }

    private boolean addedFeatureUnoccupied(Tile newTile) {
        Set<Tile> tiles = new HashSet<>(tilesAndFeatureTileDirections.keySet());
        if (newTile.hasFollower()) {
            for (Tile existingTile : tiles) {
                if (!existingTile.hasFollower())
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
        for (Tile rsTile : tiles) {
            if (rsTile.getCoordinates().equals(tile.getCoordinates()))
                return false;
        }
        return Coordinates.getAround(extractCoordinatesSet(tiles)).contains(tile.getCoordinates());
    }

    private Set<Coordinates> extractCoordinatesSet(Set<Tile> tiles) {
        Set<Coordinates> result = tiles.stream().map(Tile::getCoordinates).collect(Collectors.toSet());
        return result;
    }


    private void addIfCanBeConnectedToRealEstate(Tile tile) {
        int[][] aroundCoordinates = {{tile.getX(), tile.getY() - 1}, {tile.getX(), tile.getY() + 1},
                {tile.getX() - 1, tile.getY()}, {tile.getX() + 1, tile.getY()}};
        TileDirections[] neighbourDirection = {TileDirections.NORTH, TileDirections.SOUTH, TileDirections.WEST,
                TileDirections.EAST};

        outer:
        for (int i = 0; i < 4; i++) {
            Tile t = table.getTile(aroundCoordinates[i][0], aroundCoordinates[i][1]);
            if (!t.isNull() && tilesAndFeatureTileDirections.containsKey(t)) {
                Set<TileDirections> directionsThatBelongToRealEstate = tilesAndFeatureTileDirections.get(t);
                Set<TileDirections> targetEdge = neighbourDirection[i].getNeighbour().getEdge();

                for (TileDirections edge : targetEdge) {
                    if (directionsThatBelongToRealEstate.contains(edge)) {
                        tilesAndFeatureTileDirections.put(tile, tile.getDestinations(edge.getNeighbour()));
                        addAdjacentTiles(tile);
                        break outer;
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
                throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
            }
        } else if (neighbour.getY() == recipient.getY()) {
            if (neighbour.getX() == recipient.getX() + 1) {
                recipientBorder = TileDirections.EAST;
            } else if (neighbour.getX() == recipient.getX() - 1) {
                recipientBorder = TileDirections.WEST;
            } else {
                throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
            }

        } else {
            throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
        }

        Set<TileDirections> neighbourFeatureDirections = tilesAndFeatureTileDirections.get(neighbour);
        Set<TileDirections> edge = recipientBorder.getNeighbour().getEdge();

        neighbourFeatureDirections.retainAll(edge);
        /*
         * neighbourFeatureDirections now contains TileDirections of Feature that continues onto recipient tile
         * on the border with recipient tile
         */

        if (neighbourFeatureDirections.iterator().hasNext())
            return recipient.getDestinations(neighbourFeatureDirections.iterator().next());

        return result;
    }

    /*
     * (from the rules of the game)
     * A legitimate owner is an owner who has the most followers placed on real estate.
     * Real estate will have several legitimate owners if there is a tie.
     */
    Set<Player> getLegitimateOwners() {
        Map<Player, Integer> numberOfPlacedFollowers = new HashMap<>();
        int maximumPlayerPresence = 1;
        for (Tile tile : tilesAndFeatureTileDirections.keySet()) {
            if (tile.hasFollower()) {
                Player player = tile.getFollowerOwner();
                if (numberOfPlacedFollowers.containsKey(player)) {
                    int count = numberOfPlacedFollowers.get(player);
                    count++;
                    if (count > maximumPlayerPresence)
                        maximumPlayerPresence = count;
                    numberOfPlacedFollowers.put(player, count);
                } else {
                    numberOfPlacedFollowers.put(player, 1);
                }
            }
        }

        Set<Player> result = new HashSet<>();
        for (Player player : numberOfPlacedFollowers.keySet()) {
            if (numberOfPlacedFollowers.get(player) == maximumPlayerPresence)
                result.add(player);
        }
        return result;
    }


    public boolean contains(Tile tilePlacedLast, TileDirections direction) {
        boolean result = false;
        for (Tile tile : tilesAndFeatureTileDirections.keySet()) {
            if (tile.equals(tilePlacedLast)) {
                result = tilesAndFeatureTileDirections.get(tile).contains(direction);
            }
            if (result)
                break;
        }
        return result;
    }


    void rollBack(Tile tile) {
        tilesAndFeatureTileDirections.remove(tile);
    }

    /*
     * Method used by child class Cloister
     */
    void putTile(Tile tile, Set<TileDirections> directions) {
        tilesAndFeatureTileDirections.put(tile, directions);
    }

    @Override
    public boolean equals(Object o) {
        if (!this.tilesAndFeatureTileDirections.keySet().equals(((RealEstate) o).tilesAndFeatureTileDirections.keySet()))
            return false;
        for (Tile thisTile : tilesAndFeatureTileDirections.keySet()) {
            if (!this.tilesAndFeatureTileDirections.get(thisTile).
                    equals(((RealEstate) o).tilesAndFeatureTileDirections.get(thisTile)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1043;
        for (Tile tile : tilesAndFeatureTileDirections.keySet()) {
            hashCode += 10 * tile.hashCode();
        }
        return hashCode;
    }

    /*
     * Getters and setters begin
     */
    int getFirstX() {
        return firstTile.getX();
    }

    int getFirstY() {
        return firstTile.getY();
    }

    ImmutableRealEstate getImmutableRealEstate() {
        return immutableRealEstate;
    }

    Set<Tile> getTileSet() {
        return new HashSet<>(tilesAndFeatureTileDirections.keySet());
    }

    Tile getFirstTile() {
        return firstTile;
    }

    void setFirstTile(Tile firstTile) {
        this.firstTile = firstTile;
    }

    abstract int getPoints();

    int getNumberOfTiles() {
        return tilesAndFeatureTileDirections.keySet().size();
    }

    Map<Tile, Set<TileDirections>> getTilesAndFeatureTileDirections() {
        return tilesAndFeatureTileDirections;
    }

    public boolean isCloister() {
        return false;
    }
    /*
     * Getters and setters end
     */

    /*
     * Every RealEstate instance is mutable, so a corresponding immutableRealEstate instance is used
     * as Map key when necessary
     */
    static class ImmutableRealEstate {
        private final RealEstate realEstate;
        private final Tile firstTile;

        ImmutableRealEstate(RealEstate realEstate, Tile firstTile) {
            this.realEstate = realEstate;
            this.firstTile = firstTile;
        }

        public RealEstate getRealEstate() {
            return realEstate;
        }

        public int hashCode() {
            return (firstTile.hashCode() * 13) % 7;
        }
    }
}
