package carcassonne.model;

import carcassonne.model.realEstate.RealEstateManager;
import carcassonne.model.tile.*;
import carcassonne.view.PlacedFollower;

import java.util.*;

/*
 * Table on which table games are played, not a table that has rows and columns
 */
public class Table implements OwnershipChecker{
    private static Table table;
    private Tile currentTile;
    private Tile tilePlacedLast;
    private RealEstateManager realEstateManager;
    private final TilePlacingHelper tilePlacementHelper;
    private final Set<PlacedFollower> placedFollowers = new HashSet<>();
    /*
     * firstTile is always the same and should be placed in the center of the table before the game starts
     */
    private final Tile firstTile = Tile.getInstance(0, 0, TileName.CITY1RWE);
    private final HashMap<Coordinates, Tile> placedTiles = new HashMap<>();

    public Table() {
        tilePlacementHelper = new TilePlacingHelper(this);
        placeTile(firstTile);
    }

    public void setRealEstateManager(RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    static Table getInstance() {
        if (table == null)
            table = new Table();
        return table;
    }

    /*
     * tile should be rotated according to user input
     */
    public void placeTile(Tile tile) {
        tilePlacedLast = tile;
        if (placedTiles.containsKey(tile.getCoordinates()))
            throw new RuntimeException("Trying to place tile on an occupied space");

        placedTiles.put(tilePlacedLast.getCoordinates(), tilePlacedLast);
        tilePlacementHelper.setPlacedTiles(new HashMap<>(placedTiles));
        tilePlacementHelper.update(tilePlacedLast);

        notifyObservers(tilePlacedLast);
        if (tilePlacedLast.hasFollower()) {
            if (realEstateManager.isPartOfRealEstate(tilePlacedLast, tilePlacedLast.getFollowerTileDirection()))
                throw new RuntimeException("Cannot place follower on existing real estate");
            placedFollowers.add(new PlacedFollower(tilePlacedLast.getCoordinates(), tilePlacedLast.getOccupiedFeature()));
            realEstateManager.createAsset(tilePlacedLast.getFollowerOwner(), tilePlacedLast);
        }
    }

    private void notifyObservers(Tile tile) {
        if (realEstateManager != null)
            realEstateManager.update(tile);
    }

    /*
     * Invoked by finishedRealEstate() method of RealEstateManager
     */
    public void removeFollowerFromTile(Coordinates coordinates) {
        Iterator<PlacedFollower> iterator = placedFollowers.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getCoordinates().equals(coordinates)) {
                iterator.remove();
            }
        }
    }

    /*
     * Used by Game's method dragTile. An instance of Game uses it's instance of TilePile to
     * get a new tile and passes it to table using this method
     */
    void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
        tilePlacementHelper.update(currentTile);
    }

    @Override
    public boolean locationIsLegal(int currentTileX, int currentTileY, Rotation angle, TileDirections direction) {
        Tile temporaryTile = currentTile.setCoordinates(currentTileX, currentTileY).turnRight(angle);
        direction = direction.turnRight(angle);
        return !realEstateManager.isPartOfRealEstate(temporaryTile, direction);
    }

    //<editor-fold desc="Getters">
    public Tile getFirstTile() {
        return firstTile;
    }

    Tile getCurrentTile() {
        return currentTile;
    }

    Tile getPreviouslyPlacedTile() {
        return tilePlacedLast;
    }

    Map<Coordinates,Set<Rotation>> getPossibleTileLocationsAndRotations() {
        return tilePlacementHelper.getCoordinatesToRotationMap();
    }

    Set<PlacedFollower> getPlacedFollowers() {
        return new HashSet<>(placedFollowers);
    }


    public HashMap<Coordinates,Tile> getPlacedTiles() {
        return new HashMap<>(placedTiles);
    }

    public Tile getTile(int i, int j) {
        Coordinates coordinates = new Coordinates(i, j);
        if (placedTiles.get(coordinates) != null)
            return placedTiles.get(coordinates);
        else
            return Tile.getNullInstance();
    }

    public Tile getNeighbouringTile(int x, int y, TileDirections direction) {
        switch (direction) {
            case NNE:
            case NNW:
            case NORTH:
                return getTile(x, y - 1);
            case SSE:
            case SSW:
            case SOUTH:
                return getTile(x, y + 1);
            case WWN:
            case WWS:
            case WEST:
                return getTile(x - 1, y);
            case EEN:
            case EES:
            case EAST:
                return getTile(x + 1, y);
        }
        return Tile.getNullInstance();
    }

    void addEndGamePoints() {
        realEstateManager.addPointsForUnfinishedRealEstate();
    }
    //</editor-fold>
}
