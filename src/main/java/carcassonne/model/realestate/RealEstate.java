package carcassonne.model.realestate;

import carcassonne.model.*;
import carcassonne.model.Feature.CloisterPiece;
import carcassonne.model.Feature.LandPiece;
import carcassonne.model.Feature.RoadPiece;
import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RealEstate is a class that creates abstract objects from features of several tiles that are connected
 * to each other. Represents Land, Roads, Castles and Cloisters from the rules of Carcassonne.
 */
public abstract class RealEstate {
    protected final boolean temporary;
    private final ImmutableRealEstate immutableRealEstate;
    boolean finished = false;
    ElementsOfRealEstate elements = new ElementsOfRealEstate();
    TilesOnTable table;
    private Tile firstTile;

    public RealEstate(Tile tile, TilesOnTable table) {
        if (!tile.hasFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        firstTile = tile;
        this.table = table;
        this.temporary = false;
        immutableRealEstate = new ImmutableRealEstate(this, tile);
        addFirstTile(tile);
    }

    public RealEstate(Tile tile, TilesOnTable table, boolean temporary) {
        if (!tile.hasFollower())
            throw new RuntimeException("Cannot create real estate from tile without a follower");
        this.firstTile = tile;
        this.table = table;
        this.immutableRealEstate = new ImmutableRealEstate(this, tile);
        this.temporary = temporary;
        addFirstTile(tile);
    }

    static RealEstate getInstance(Tile tile, TilesOnTable table) {
        if (tile.getOccupiedFeature() instanceof RoadPiece)
            return new Road(tile, table);
        if (tile.getOccupiedFeature() instanceof CloisterPiece)
            return new Cloister(tile, table);
        if (tile.getOccupiedFeature() instanceof LandPiece)
            return new Land(tile, table);

        return new Castle(tile, table);
    }

    public RealEstate getTemporaryRealEstate() {
//        Tile tile = realEstate.getFirstTile();
//        if (tile.getOccupiedFeature() instanceof RoadPiece)
//            return new Road(tile, table, false);
//        if (tile.getOccupiedFeature() instanceof CloisterPiece)
//            return new Cloister(tile, table, false);
//        if (tile.getOccupiedFeature() instanceof LandPiece)
//            return new Land(tile, table, false);
//
//        return new Castle(tile, table, false);

        return RealEstate.getInstance(this.firstTile, this.table);

    }

    TilesOnTable getTable() {
        return table;
    }

    public void setTable(Table table) {
        table = table;
    }

    abstract boolean isFinished();

    public void addFirstTile(Tile tile) {
        checkArguments(tile);
        addTileAndConnectedTiles(tile);
    }

    public void update(Tile tile) {
        if (!tile.equals(firstTile))
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
        elements = elements.add(tile, tile.getOccupiedFeatureDirections());
        addAdjacentTiles(tile);
    }

    private void addAdjacentTiles(Tile tile) {
        Map<Tile, Set<TileDirection>> adjacentTiles = new HashMap<>();
        Set<TileDirection> occupiedFeatureDirections = elements.getTileDirectionSet(tile);
        assert (occupiedFeatureDirections != null);

        for (TileDirection tileDirection : occupiedFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), tileDirection);
            if (!neighbour.isNull() && !elements.contains(neighbour)) {
                Util.addLinkedSetElement(adjacentTiles, neighbour, neighbour.getDestinations(tileDirection.getNeighbour()));
                Util.addAllSetElements(adjacentTiles, findAdjacentTiles(neighbour, tileDirection.getNeighbour(), new HashSet<>()));
            }
        }

        elements = elements.addAll(adjacentTiles);
    }

    /*
     * Method uses recursion
     */
    private Map<Tile, Set<TileDirection>> findAdjacentTiles(Tile startTile, TileDirection directionWithFeature, Set<Tile> visitedTiles) {
        Map<Tile, Set<TileDirection>> result = new HashMap<>();

        Set<TileDirection> currentTileFeatureDirections = startTile.getDestinations(directionWithFeature);
        currentTileFeatureDirections.removeAll(directionWithFeature.getEdge());
        for (TileDirection direction : currentTileFeatureDirections) {
            Tile neighbour = table.getNeighbouringTile(startTile.getX(), startTile.getY(), direction);

            if (!neighbour.isNull() && !visitedTiles.contains(neighbour) && !elements.contains(neighbour)) {
                visitedTiles.add(neighbour);
                result.put(neighbour, neighbour.getDestinations(direction.getNeighbour()));
                result.putAll(findAdjacentTiles(neighbour, direction.getNeighbour(), visitedTiles));
            }
        }
        return result;
    }

    private boolean addedFeatureUnoccupied(Tile newTile) {
        Set<Tile> tiles = elements.getTileSet();
        if (newTile.hasFollower()) {
            for (Tile existingTile : tiles) {
                if (!existingTile.hasFollower())
                    return true;

                Set<TileDirection> existingDirections = existingTile.getOccupiedFeatureDirections();
                for (TileDirection existing : existingDirections) {
                    TileDirection shouldBeUnoccupied = existing.getNeighbour();
                    if (newTile.getOccupiedFeatureDirections().contains(shouldBeUnoccupied))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean validCoordinates(Tile tile) {
        Set<Tile> tiles = elements.getTileSet();
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
        TileDirection[] neighbourDirection = {TileDirection.NORTH, TileDirection.SOUTH, TileDirection.WEST,
                TileDirection.EAST};

        outer:
        for (int i = 0; i < 4; i++) {
            Tile t = table.getTile(aroundCoordinates[i][0], aroundCoordinates[i][1]);
            if (!t.isNull() && elements.contains(t)) {
                Set<TileDirection> directionsThatBelongToRealEstate = elements.getTileDirectionSet(t);
                Set<TileDirection> targetEdge = neighbourDirection[i].getNeighbour().getEdge();

                for (TileDirection edge : targetEdge) {
                    if (directionsThatBelongToRealEstate.contains(edge)) {
                        elements = elements.add(tile, tile.getDestinations(edge.getNeighbour()));
                        addAdjacentTiles(tile);
                        break outer;
                    }
                }
            }
        }
    }

    private Set<TileDirection> getNeighbourFeaturesContinuation(Tile neighbour, Tile recipient) {
        Set<TileDirection> result = new HashSet<>();
        TileDirection recipientBorder;
        if (neighbour.getX() == recipient.getX()) {
            if (neighbour.getY() == recipient.getY() + 1) {
                recipientBorder = TileDirection.SOUTH;
            } else if (neighbour.getY() == recipient.getY() - 1) {
                recipientBorder = TileDirection.NORTH;
            } else {
                throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
            }
        } else if (neighbour.getY() == recipient.getY()) {
            if (neighbour.getX() == recipient.getX() + 1) {
                recipientBorder = TileDirection.EAST;
            } else if (neighbour.getX() == recipient.getX() - 1) {
                recipientBorder = TileDirection.WEST;
            } else {
                throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
            }

        } else {
            throw new RuntimeException("Trying to get continuation of Feature from illegally placed tiles");
        }

        Set<TileDirection> neighbourFeatureDirections = elements.getTileDirectionSet(neighbour);
        Set<TileDirection> edge = recipientBorder.getNeighbour().getEdge();

        neighbourFeatureDirections.retainAll(edge);
        /*
         * neighbourFeatureDirections now contains TileDirection of Feature that continues onto recipient tile
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
        for (Tile tile : elements.getTileSet()) {
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


    public boolean contains(Tile tilePlacedLast, TileDirection direction) {
        boolean result = false;
        for (Tile tile : elements.getTileSet()) {
            if (tile.equals(tilePlacedLast)) {
                result = elements.getTileDirectionSet(tile).contains(direction);
            }
            if (result)
                break;
        }
        return result;
    }

    /*
     * Method used by child class Cloister
     */
    void putTile(Tile tile, Set<TileDirection> directions) {
        elements = elements.add(tile, directions);
    }

    @Override
    public boolean equals(Object o) {
        if (!this.elements.getTileSet().equals(((RealEstate) o).elements.getTileSet()))
            return false;
        for (Tile thisTile : elements.getTileSet()) {
            if (!this.elements.getTileDirectionSet(thisTile).
                    equals(((RealEstate) o).elements.getTileDirectionSet(thisTile)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1043;
        for (Tile tile : elements.getTileSet()) {
            hashCode += 10 * tile.hashCode();
        }
        return hashCode;
    }

    //<editor-fold desc="Getters and setters">
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
        return new HashSet<>(elements.getTileSet());
    }

    Tile getFirstTile() {
        return firstTile;
    }

    void setFirstTile(Tile firstTile) {
        this.firstTile = firstTile;
    }

    abstract int getPoints();

    int getNumberOfTiles() {
        return elements.numberOfTiles();
    }

    Map<Tile, Set<TileDirection>> getTilesAndFeatureTileDirections() {
        return elements.getElementsMap();
    }

    public boolean isCloister() {
        return false;
    }
    //</editor-fold>

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
