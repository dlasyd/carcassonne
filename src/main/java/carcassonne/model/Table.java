package carcassonne.model;

import carcassonne.model.realestate.RealEstateManager;
import carcassonne.model.tile.*;
import carcassonne.view.PlacedFollower;

import java.util.*;

/*
 * Table on which table games are played, not a table that has rows and columns
 */
public class Table implements OwnershipChecker, TilesOnTable {
    private static Table table;
    /*
     * firstTile is always the same and should be placed in the center of the table before the game starts
     */
    private final Tile firstTile = Tile.getInstance(0, 0, TileName.CITY1RWE);
    private final TilePlacingHelper tilePlacementHelper;
    private final Set<PlacedFollower> placedFollowers = new HashSet<>();
    private final HashMap<Coordinates, Tile> placedTiles = new HashMap<>();
    private Tile currentTile;
    private Tile tilePlacedLast;
    private RealEstateManager realEstateManager;

    public Table() {
        tilePlacementHelper = new TilePlacingHelper(this);
        placeTile(firstTile);
    }

    static Table getInstance() {
        if (table == null)
            table = new Table();
        return table;
    }

    public void setRealEstateManager(RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    /*
     * tile should be rotated according to user input
     */
    public void placeTile(Tile tile) {
        exceptionIfInvalidTile(tile);

        tilePlacedLast = tile;
        placedTiles.put(tilePlacedLast.getCoordinates(), tilePlacedLast);
        tilePlacementHelper.setPlacedTiles(new HashMap<>(placedTiles));

        if (tilePlacedLast.hasFollower()) {
            placedFollowers.add(new PlacedFollower(tilePlacedLast.getCoordinates(), tilePlacedLast.getOccupiedFeature()));
            realEstateManager.createAsset(tilePlacedLast.getFollowerOwner(), tilePlacedLast);
        }

        notifyObservers(tilePlacedLast);
    }

    private void exceptionIfInvalidTile(Tile tile) {
        if (placedTiles.containsKey(tile.getCoordinates()))
            throw new RuntimeException("Trying to place tile on an occupied space");
        if (tile.hasFollower() && realEstateManager.isPartOfRealEstate(tile, tile.getFollowerTileDirection())) {
            throw new RuntimeException("Cannot place follower on existing real estate");
        }
    }

    private void notifyObservers(Tile tile) {
        if (realEstateManager != null)
            realEstateManager.update(tile);
        if (tilePlacementHelper != null)
            tilePlacementHelper.update(tile);
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

    @Override
    public boolean locationIsLegal(int currentTileX, int currentTileY, Rotation angle, TileDirection direction) {
        Tile temporaryTile = currentTile.setCoordinates(currentTileX, currentTileY).turnClockwise(angle);
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

    /*
     * Used by Game's method dragTile. An instance of Game uses it's instance of TilePile to
     * get a new tile and passes it to table using this method
     */
    void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
        tilePlacementHelper.update(currentTile);
    }

    Tile getPreviouslyPlacedTile() {
        return tilePlacedLast;
    }

    Map<Coordinates, Set<Rotation>> getPossibleTileLocationsAndRotations() {
        return tilePlacementHelper.getCoordinatesToRotationMap();
    }

    Set<PlacedFollower> getPlacedFollowers() {
        return new HashSet<>(placedFollowers);
    }


    public HashMap<Coordinates, Tile> getPlacedTiles() {
        return new HashMap<>(placedTiles);
    }

    public Tile getTile(int i, int j) {
        Coordinates coordinates = new Coordinates(i, j);
        if (placedTiles.get(coordinates) != null)
            return placedTiles.get(coordinates);
        else
            return Tile.getNullInstance();
    }

    public Tile getNeighbouringTile(int x, int y, TileDirection direction) {
        Coordinates c = direction.getNeighbourCoordinates(x, y);
        return getTile(c.getX(), c.getY());
    }

    void addEndGamePoints() {
        realEstateManager.addPointsForUnfinishedRealEstate();
    }
    //</editor-fold>
}
